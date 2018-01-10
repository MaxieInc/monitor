package ru.dtnm.monitor.mail;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Map;

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
import ru.dtnm.monitor.model.config.alert.AlertTemplateType;

@Component
public class MailSendImpl implements MailSend {

    private static final Logger LOG = LoggerFactory.getLogger(MailSend.class);

    @Value("${mail.from.address}")
    private String mailFromAddress;

    @Value("${mail.from.name}")
    private String mailFromName;

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * ОТправка электронного письма
     *
     * @param component мнемо компонента
     * @param action событие
     * @param email почта
     * @param templates шаблоны писем
     */
    @Override
    public void sendMessage(
            final String component,
            final AlertAction action,
            final String email,
            final Map<String, String> templates) {
        LOG.debug(">> sendMessage to {} with action: {}", email, action);
        final MimeMessage message = javaMailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(mailFromAddress, mailFromName);
            helper.setTo(email);

            // Тема письма
            final String subject = templates.get(AlertTemplateType.COMPONENT_STATUS_CHANGED_SUBJECT.name());
            helper.setSubject(subject);

            // Тело письма
            final String bodyTemplate = templates.get(AlertTemplateType.COMPONENT_STATUS_CHANGED_BODY.name());
            final String body = MessageFormat.format(bodyTemplate, component, action.getStatus());
            helper.setText(body, true);

            javaMailSender.send(message);
            LOG.debug("<< sendMessage");
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOG.debug("Unable to send email because of {}", e.getMessage());
        }
    }
}