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
import ru.dtnm.monitor.model.config.alert.AlertConfig;
import ru.dtnm.monitor.model.config.alert.AlertConfigContainer;
import ru.dtnm.monitor.model.config.component.ComponentConfig;
import ru.dtnm.monitor.model.config.component.ComponentQueryType;
import ru.dtnm.monitor.model.config.component.MonitorConfig;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private String monitorConfigLocation;

    @Value("${alert.config.location:null}")
    private String alertConfigLocation;

    @Value("${ssl.restrictions.ignore:false}")
    private boolean sslIgnoreSetting;


    /** Мап сущностей, опрашивающих удалённые компоненты*/
    private static final Map<String, Checker> CHECKERS = new HashMap<>();

    @PostConstruct
    private void init() {
        reloadConfig();
    }

    /**
     * Читает конфигурацию из файла и возвращает соответствующий класс
     *
     * @param configLocation расположение файла конфигурации
     * @param clazz класс - контейнер
     * @param <T> класс - контейнер
     * @throws IOException
     */
    private <T> T getConfig(final String configLocation, final Class<T> clazz) throws IOException {
        final String configJson = IOUtils.toString(new FileInputStream(configLocation));
        return objectMapper.readValue(configJson, clazz);
    }

    public Map<String, Object> readConfigs() throws IOException {
        return new HashMap<String, Object>(){{
            put("monitor", getConfig(monitorConfigLocation, MonitorConfig.class));
            put("alert", getConfig(alertConfigLocation, AlertConfigContainer.class));
        }};
    }

    /**
     * Возвращает конфигурацию указанного компонента
     *
     * @param mnemo мнемо компонента
     */
    public ComponentConfig getConfigByMnemo(final String mnemo) throws IOException {
        return getConfig(monitorConfigLocation, MonitorConfig.class)
                .getComponents()
                .stream()
                .filter(e -> e.getMnemo().equals(mnemo))
                .findFirst()
                .get();
    }

    /**
     * Возвращает конфигурацию оповещений для указанного компонента
     *
     * @param mnemo мнемо компонента
     */
    public AlertConfig getAlertConfigByMnemo(final String mnemo) throws IOException {
        return getConfig(alertConfigLocation, AlertConfigContainer.class)
                .getAlerts()
                .stream()
                .filter(e -> e.getComponent().equals(mnemo))
                .findFirst()
                .get();
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
    public void reloadConfig() {
        blocked = true;
        CHECKERS.clear();
        try {
            // конфиг оповещений
            final Map<String, AlertConfig> alertConfigs = getConfig(alertConfigLocation, AlertConfigContainer.class)
                    .getAlerts()
                    .stream()
                    .collect(Collectors.toMap(AlertConfig::getComponent, e -> e));
            // Конфиг опрашиваемых компонентов
            getConfig(monitorConfigLocation, MonitorConfig.class)
                    .getComponents()
                    .forEach(e -> CHECKERS.put(e.getMnemo(), new SimpleChecker(e, alertConfigs.get(e.getMnemo()), sslIgnoreSetting)));
        } catch (IOException ioe) {
            LOG.error("Unable to read configJson: {}", ioe.getMessage());
        }
        blocked = false;
    }

    @Scheduled(fixedDelayString = "${monitor.delay}")
    private void pingAll() {
        if (!blocked) {
            for (Checker checker : CHECKERS.values()) {
                if (ComponentQueryType.QUERY.equals(checker.getComponentConfig().getType())) {
                    try {
                        checker.check(historyHandler);
                    } catch (Exception e) {
                        LOG.error("Unable to query component {}", checker.getComponentConfig().getMnemo(), e.getMessage());
                    }
                }
            }
        }
    }
}
