package ru.dtnm.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.helpers.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.dtnm.monitor.checker.Checker;
import ru.dtnm.monitor.checker.SimpleChecker;
import ru.dtnm.monitor.history.HistoryHandler;
import ru.dtnm.monitor.model.CheckResult;
import ru.dtnm.monitor.model.config.ConfigComponent;
import ru.dtnm.monitor.model.config.MonitorConfig;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Контейнер опрашивающих компонентов
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
@Component
@EnableScheduling
public class CheckerContainer {

    private static final Logger LOG = LoggerFactory.getLogger(CheckerContainer.class);

    /** Признак запрета опроса на время обновления конфигурации */
    private static boolean blocked = false;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HistoryHandler historyHandler;

    @Value("${monitor.config.location:null}")
    private String configFileLocation;

    /**
     * Мап сущностей, опрашивающих удалённые компоненты
     */
    private static final Map<String, Checker> CHECKERS = new HashMap<>();

    @PostConstruct
    private void init() {
        readConfig();
    }

    /**
     * Читает текущую конфигурацию из конфигурационного JSONа
     *
     * @throws IOException
     */
    public MonitorConfig getMonitorConfig() throws IOException{
        final String configJson = IOUtils.toString(new FileInputStream(configFileLocation));
        return objectMapper.readValue(configJson, MonitorConfig.class);
    }

    /**
     * Возвращает набор мнемоник зарегистрированных компонентов
     */
    public Set<String> getRegisteredMnemos() {
        return CHECKERS.keySet();
    }

    /**
     * Обновление конфигурации
     */
    public void readConfig() {
        blocked = true;
        CHECKERS.clear();
        try {
            final MonitorConfig config = getMonitorConfig();
            for (ConfigComponent component : config.getComponents()) {
                final SimpleChecker checker = new SimpleChecker(component.getMnemo(), component.getUrl());
                CHECKERS.put(checker.getMnemo(), checker);
            }
        } catch (IOException ioe) {
            LOG.error("Unable to read configJson: {}", ioe.getMessage());
        }
        blocked = false;
    }

    @Scheduled(fixedDelayString = "${monitor.delay}")
    private void pingAl() {
        if (!blocked) {
            for (Checker checker : CHECKERS.values()) {
                checker.check(historyHandler);
            }
        }
    }
}
