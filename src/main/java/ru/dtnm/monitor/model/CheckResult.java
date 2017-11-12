package ru.dtnm.monitor.model;

/**
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class CheckResult {

    private CheckStatus status;

    public CheckStatus getStatus() {
        return status;
    }

    public void setStatus(CheckStatus status) {
        this.status = status;
    }
}
