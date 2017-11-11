package ru.dtnm.monitor.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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

    private static final Logger LOG = LoggerFactory.getLogger(AlertHandler.class);

    public void notify(final String component, final AlertAction action) {
        LOG.debug(">> notify: login={}, component={}, status={}", action.getLogin(), component, action.getStatus());
        // todo реализация самого уведомления
    }
}
