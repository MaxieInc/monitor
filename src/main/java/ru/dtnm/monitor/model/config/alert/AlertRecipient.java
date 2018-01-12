package ru.dtnm.monitor.model.config.alert;

/**
 * Адресат рассылки уведомлений
 */
public class AlertRecipient {

    private String login;
    private boolean email;
    private boolean sms;

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
