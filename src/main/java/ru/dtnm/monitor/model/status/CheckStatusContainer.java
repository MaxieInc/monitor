package ru.dtnm.monitor.model.status;

import java.io.Serializable;

public class CheckStatusContainer implements Serializable {

    private CheckStatus status;
    private String reason;

    public CheckStatusContainer() {
    }

    public CheckStatusContainer(CheckStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public CheckStatus getStatus() {
        return status;
    }

    public CheckStatusContainer setStatus(CheckStatus status) {
        this.status = status;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public CheckStatusContainer setReason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Сравнивает два статуса, возвращает "худший"
     * чем больше номер, тем "хуже" статус
     *
     * @param container сравниваемый статус
     */
    public CheckStatusContainer getWorst(final CheckStatusContainer container) {
        if (this.getStatus().getNumber() >= container.getStatus().getNumber()) return this;
        else return container;
    }
}
