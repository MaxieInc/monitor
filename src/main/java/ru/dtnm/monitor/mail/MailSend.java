package ru.dtnm.monitor.mail;

import ru.dtnm.monitor.model.config.alert.AlertAction;

import java.util.Map;

public interface MailSend {

	String ATTR_EMAIL = "email";				// Адрес электронной почты

    void sendMessage(String component, AlertAction action, String email, Map<String, String> templates);
}
