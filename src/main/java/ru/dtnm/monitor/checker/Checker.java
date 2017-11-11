package ru.dtnm.monitor.checker;

import org.apache.http.HttpStatus;
import ru.dtnm.monitor.history.HistoryHandler;
import ru.dtnm.monitor.model.CheckResult;
import ru.dtnm.monitor.model.CheckStatus;
import ru.dtnm.monitor.model.config.alert.AlertConfig;
import ru.dtnm.monitor.model.config.component.ComponentInfo;
import ru.dtnm.monitor.notification.AlertHandler;

import java.util.Date;

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

    /**
     * Конструктор результата
     *
     * @param httpStatus статус ответа
     * @param start время начала опроса
     * @param end время окончания опроса
     */
    public CheckResult getResult(int httpStatus, final Date start, final Date end) {
        return new CheckResult()
                .withMnemo(componentInfo.getMnemo())
                .withUrl(componentInfo.getUrl())
                .withLastResponseDuration(end.getTime() - start.getTime())
                .withLastResponse(end)
                .withStatus(HttpStatus.SC_OK == httpStatus ? CheckStatus.HEALTHY : CheckStatus.FAILED);
    }

    public CheckResult getExceptionResult(final String comment) {
        return new CheckResult()
                .withMnemo(componentInfo.getMnemo())
                .withUrl(componentInfo.getUrl())
                .withStatus(CheckStatus.FAILED)
                .withComment(comment);
    }

    public Checker(ComponentInfo componentInfo, AlertConfig alertConfig) {
        this.componentInfo = componentInfo;
        this.alertConfig = alertConfig;
    }
}
