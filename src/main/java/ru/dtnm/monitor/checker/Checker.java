package ru.dtnm.monitor.checker;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.dtnm.monitor.history.HistoryHandler;
import ru.dtnm.monitor.model.config.alert.AlertConfig;
import ru.dtnm.monitor.model.config.component.ComponentConfig;

/**
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public abstract class Checker {

    protected static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected ComponentConfig componentConfig;
    protected AlertConfig alertConfig;
    protected boolean ignoreSSL;

    /** Возвращает собственную конфигурацию компонента */
    public ComponentConfig getComponentConfig() {
        return componentConfig;
    }

    public void setComponentConfig(ComponentConfig componentConfig) {
        this.componentConfig = componentConfig;
    }

    /** Возвращает конфигурацию уведомлений по компоненту */
    public AlertConfig getAlertConfig() {
        return alertConfig;
    }

    public void setAlertConfig(AlertConfig alertConfig) {
        this.alertConfig = alertConfig;
    }

    public abstract void check(HistoryHandler historyHandler) throws Exception;

    public Checker(ComponentConfig componentConfig, AlertConfig alertConfig, boolean ignoreSSL) {
        this.componentConfig = componentConfig;
        this.alertConfig = alertConfig;
        this.ignoreSSL = ignoreSSL;
    }
}
