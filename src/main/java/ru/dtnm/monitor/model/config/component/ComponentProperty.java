package ru.dtnm.monitor.model.config.component;

import java.io.Serializable;

/**
 * <p>Конфиг: Строковое свойство компонента для мониторинга</p>
 * @author Яковлев В.Л.
 */
public class ComponentProperty implements Serializable {

    private String mnemo;
    private String value;
    private boolean mandatory;
    private String special;

    /** [Обязательный] Строковый мнемокод строкового свойства компонента */
    public String getMnemo() {
        return mnemo;
    }

    public void setMnemo(String mnemo) {
        this.mnemo = mnemo;
    }

    /** [Необязательное] Строковое значение строкового свойства компонента */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /** [Обязательный] Признак обязательности строкового свойства компонента */
    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /** [Необязательное] Указание на особое свойство компонента (address, hostname, version) */
    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }
}
