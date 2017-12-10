package ru.dtnm.monitor.mail;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ru.dtnm.monitor.model.config.alert.AlertAction;

@Component
public class MailSendImpl implements MailSend {

    private static final Logger LOG = LoggerFactory.getLogger(MailSend.class);
    private static final String CHANGE_STATUS_BODY = "Внимание! Статус компонента %s изменился на %s";
    private static final String CHANGE_STATUS_SUBJECT = "Изменение статуса компонента";

    @Value("${mail.from.address}")
    private String mailFromAddress;

    @Value("${mail.from.name}")
    private String mailFromName;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMessage(final String component, final AlertAction action) {
        LOG.debug(">> sendMessage to {} with action: {}", action.getLogin(), action);
        final MimeMessage message = javaMailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(mailFromAddress, mailFromName);
            helper.setTo(action.getLogin());
            final String subject = CHANGE_STATUS_SUBJECT;
            helper.setSubject(subject);
            final String body = String.format(CHANGE_STATUS_BODY, component, action.getStatus());
            helper.setText(body, true);
            javaMailSender.send(message);
            LOG.debug("<< sendMessage");
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOG.debug("Unable to send email because of {}", e.getMessage());
        }
    }
}