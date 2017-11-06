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
import ru.dtnm.monitor.checker.SimpleChecker;
import ru.dtnm.monitor.history.HistoryHandler;
import ru.dtnm.monitor.model.config.ConfigComponent;
import ru.dtnm.monitor.model.config.MonitorConfig;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HistoryHandler historyHandler;

    @Value("${monitor.config.location:null}")
    private String configFileLocation;

    /**
     * Мап сущностей, опрашивающих удалённые компоненты
     */
    private static final Map<String, SimpleChecker> CHECKERS = new HashMap<>();

    @PostConstruct
    private void init() {
        try {
            final String configJson = IOUtils.toString(new FileInputStream(configFileLocation));
            final MonitorConfig config = objectMapper.readValue(configJson, MonitorConfig.class);

            for (ConfigComponent component : config.getComponents()) {
                final SimpleChecker checker = new SimpleChecker(component.getMnemo(), component.getUrl());
                CHECKERS.put(checker.getMnemo(), checker);
            }
        } catch (IOException ioe) {
            LOG.error("Unable to read configJson: {}", ioe.getMessage());
        }
    }

    @Scheduled(fixedDelay = 5000)
    private void pingAl() {
        for (SimpleChecker checker : CHECKERS.values()) {
            checker.check(historyHandler);
        }
    }
}
