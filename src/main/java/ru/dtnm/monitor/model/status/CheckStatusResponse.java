package ru.dtnm.monitor.model.status;

import ru.dtnm.monitor.model.query.MonitoringResult;

/**
 * Транспорт: статус мониторинга компонента
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class CheckStatusResponse {

    private CheckStatus status;
    private MonitoringResult lastResponse;

    public CheckStatus getStatus() {
        return status;
    }

    public CheckStatusResponse setStatus(CheckStatus status) {
        this.status = status;
        return this;
    }

    public MonitoringResult getLastResponse() {
        return lastResponse;
    }

    public CheckStatusResponse setLastResponse(MonitoringResult lastResponse) {
        this.lastResponse = lastResponse;
        return this;
    }
}
