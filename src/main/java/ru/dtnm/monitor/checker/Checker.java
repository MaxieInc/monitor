package ru.dtnm.monitor.checker;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.dtnm.monitor.history.HistoryHandler;
import ru.dtnm.monitor.model.config.alert.AlertConfig;
import ru.dtnm.monitor.model.config.alert.AlertPerson;
import ru.dtnm.monitor.model.config.component.ComponentConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public abstract class Checker {

    protected static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected ComponentConfig componentConfig;
    protected Map<String, AlertPerson> persons;
    protected Map<String, String> templates;
    protected AlertConfig alertConfig;
    protected boolean ignoreSSL;

    /** Возвращает собственную конфигурацию компонента */
    public ComponentConfig getComponentConfig() {
        return componentConfig;
    }

    public void setComponentConfig(ComponentConfig componentConfig) {
        this.componentConfig = componentConfig;
    }

    public Map<String, AlertPerson> getPersons() {
        return persons;
    }

    public void setPersons(Map<String, AlertPerson> persons) {
        this.persons = persons;
    }

    public Map<String, String> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, String> templates) {
        this.templates = templates;
    }

    public AlertConfig getAlertConfig() {
        return alertConfig;
    }

    public void setAlertConfig(AlertConfig alertConfig) {
        this.alertConfig = alertConfig;
    }

    public boolean isIgnoreSSL() {
        return ignoreSSL;
    }

    public void setIgnoreSSL(boolean ignoreSSL) {
        this.ignoreSSL = ignoreSSL;
    }

    public abstract void check(HistoryHandler historyHandler) throws Exception;

    public Checker(
            final ComponentConfig componentConfig,
            final AlertConfig alertConfig,
            final Map<String, String> templates,
            final List<AlertPerson> persons,
            boolean ignoreSSL) {
        this.componentConfig = componentConfig;
        this.alertConfig = alertConfig;
        this.persons = new HashMap<>();
        if (persons != null) {
            for (AlertPerson person : persons) {
                this.persons.put(person.getLogin(), person);
            }
        }
        this.templates = templates;
        this.ignoreSSL = ignoreSSL;
    }
}
