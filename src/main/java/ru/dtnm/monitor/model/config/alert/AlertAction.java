package ru.dtnm.monitor.model.config.alert;

import ru.dtnm.monitor.model.status.CheckStatus;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Конфиг: Способ уведомления ответственных по компонентам </p>
 * @author Яковлев В.Л.
 */
public class AlertAction implements Serializable {

    private CheckStatus status;
    private List<AlertRecipient> recipients;


    /** [Обязательный] Строковый статус компонента (HEALTHY, WARNING, CRITICAL, FAILED) */
    public CheckStatus getStatus() {
        return status;
    }

    public void setStatus(CheckStatus status) {
        this.status = status;
    }

    /** [Необязательный] список адресатов уведомлений**/
    public List<AlertRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<AlertRecipient> recipients) {
        this.recipients = recipients;
    }
}
