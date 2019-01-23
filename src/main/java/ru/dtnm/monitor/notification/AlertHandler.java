package ru.dtnm.monitor.notification;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.dtnm.monitor.mail.MailSend;
import ru.dtnm.monitor.model.config.alert.AlertAction;
import ru.dtnm.monitor.model.config.alert.AlertPerson;
import ru.dtnm.monitor.model.config.alert.AlertRecipient;

/**
 * Компонент для отправки уведомлений об изменении статуса компонента
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
@Component
public class AlertHandler {

    @Autowired
    private MailSend mailSend;

    private static final Logger LOG = LoggerFactory.getLogger(AlertHandler.class);

    public void notify(
            final String component,
            final List<AlertAction> actions,
            final Map<String, String> templates, final Map<String, AlertPerson> persons, final String reason) {
        LOG.debug(">> notify: component={}, status={}, actions={}", component, actions.get(0).getStatus(), actions);

        for (AlertAction action : actions) {
            for (AlertRecipient recepient : action.getRecipients()) {
                if (recepient.isEmail()) {
                    mailSend.sendMessage(component, action, persons.get(recepient.getLogin()).getEmail(), templates, reason);
                }
            }
        }
    }
}
