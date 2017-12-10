package ru.dtnm.monitor.notification;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.dtnm.monitor.mail.MailSend;
import ru.dtnm.monitor.model.config.alert.AlertAction;

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

    public void notify(final String component, final List<AlertAction> actions) {
        LOG.debug(">> notify: component={}, status={}, actions={}", component, actions.get(0).getStatus(), actions);
        // todo реализация самого уведомления
        actions.stream().forEach(e -> mailSend.sendMessage(component, e));
    }
}
