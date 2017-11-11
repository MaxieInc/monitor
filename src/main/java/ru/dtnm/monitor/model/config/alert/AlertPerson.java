package ru.dtnm.monitor.model.config.alert;

import java.io.Serializable;

/**
 * <p>Конфиг: Информация об уведомляемом (ответственном) лице </p>
 * @author Яковлев В.Л.
 */
public class AlertPerson implements Serializable {

    private String login;
    private String name;
    private String email;
    private int sms;

    /** [Обязательные] Уникальный строковый логин уведомляемого */
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    /** [Обязательные] Фамилия И(мя) О(тчество) уведомляемого */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** [Обязательный] Электронный адрес для оповещения по E-mail */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /** [Обязательный] Числовой номер телефона (10 цифр) для оповещения по SMS */
    public int getSms() {
        return sms;
    }

    public void setSms(int sms) {
        this.sms = sms;
    }
}
