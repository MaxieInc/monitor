package ru.dtnm.monitor.checker;

import ru.dtnm.monitor.history.HistoryHandler;
import ru.dtnm.monitor.model.config.alert.AlertConfig;
import ru.dtnm.monitor.model.config.component.ComponentInfo;
import ru.dtnm.monitor.notification.AlertHandler;

/**
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public abstract class Checker {

    protected ComponentInfo componentInfo;
    protected AlertConfig alertConfig;

    /** Возвращает собственную конфигурацию компонента */
    public ComponentInfo getComponentInfo() {
        return componentInfo;
    }

    public void setComponentInfo(ComponentInfo componentInfo) {
        this.componentInfo = componentInfo;
    }

    /** Возвращает конфигурацию уведомлений по компоненту */
    public AlertConfig getAlertConfig() {
        return alertConfig;
    }

    public void setAlertConfig(AlertConfig alertConfig) {
        this.alertConfig = alertConfig;
    }

    public abstract void check(HistoryHandler historyHandler, final AlertHandler alertHandler);

    public Checker(ComponentInfo componentInfo, AlertConfig alertConfig) {
        this.componentInfo = componentInfo;
        this.alertConfig = alertConfig;
    }
}
