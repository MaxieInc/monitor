package ru.dtnm.monitor.model.config.alert;

import ru.dtnm.monitor.model.CheckStatus;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>Конфиг: Конфигурация уведомлений ответственных по компонентам</p>
 * @author Яковлев В.Л.
 */
public class AlertConfig implements Serializable {

    private String component;
    private Collection<AlertAction> actions;

    /** [Обязательный] Строковый мнемокод компонента */
    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    /** [Обязательный] Способы уведомления ответственных лиц */
    public Collection<AlertAction> getActions() {
        return actions;
    }

    public void setActions(Collection<AlertAction> actions) {
        this.actions = actions;
    }
}
