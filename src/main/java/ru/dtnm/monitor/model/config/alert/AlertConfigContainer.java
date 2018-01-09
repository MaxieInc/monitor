package ru.dtnm.monitor.model.config.alert;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class AlertConfigContainer implements Serializable {

    /** Описание адресатов уведомлений */
    private List<AlertPerson> persons;

    /** Настройки рассылки уведомлений */
    private List<AlertConfig> alerts;

    /** Шаблоны уведомлений по мнемоникам */
    private Map<String, String> templates;

    public List<AlertPerson> getPersons() {
        return persons;
    }

    public void setPersons(List<AlertPerson> persons) {
        this.persons = persons;
    }

    public List<AlertConfig> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<AlertConfig> alerts) {
        this.alerts = alerts;
    }

    public Map<String, String> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, String> templates) {
        this.templates = templates;
    }
}
