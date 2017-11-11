package ru.dtnm.monitor.model.config.alert;

import ru.dtnm.monitor.model.CheckStatus;

import java.io.Serializable;

/**
 * <p>Конфиг: Способ уведомления ответственных по компонентам </p>
 * @author Яковлев В.Л.
 */
public class AlertAction implements Serializable {

    private CheckStatus status;
    private String login;
    private boolean email;
    private boolean sms;

    /** [Обязательный] Строковый статус компонента (HEALTHY, WARNING, CRITICAL, FAILED) */
    public CheckStatus getStatus() {
        return status;
    }

    public void setStatus(CheckStatus status) {
        this.status = status;
    }

    /** [Обязательный] Строковый логин уведомляемого ответственного лица */
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    /** [Обязательный] Признак необходимости увебомления по E-mail */
    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    /** [Обязательный] Признак необходимости увебомления по SMS */
    public boolean isSms() {
        return sms;
    }

    public void setSms(boolean sms) {
        this.sms = sms;
    }
}
