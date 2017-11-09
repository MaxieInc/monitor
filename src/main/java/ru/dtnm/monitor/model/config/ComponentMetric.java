package ru.dtnm.monitor.model.config;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>Конфиг: Числовая метрика компонента для мониторинга</p>
 * @author Яковлев В.Л.
 */
public class ComponentMetric implements Serializable {

    private String mnemo;
    private boolean mandatory;
    private Collection<Float> healthy;
    private Collection<Float> warning;
    private Collection<Float> critical;
    private Collection<Float> failed;

    /** [Обязательный] Строковый мнемокод числовой метрики компонента */
    public String getMnemo() {
        return mnemo;
    }

    public void setMnemo(String mnemo) {
        this.mnemo = mnemo;
    }

    /** [Обязательный] Признак обязательности числовой метрики компонента */
    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /** [Обязательный] Интервал значений для состояния: "Зеленый" */
    public Collection<Float> getHealthy() {
        return healthy;
    }

    public void setHealthy(Collection<Float> healthy) {
        this.healthy = healthy;
    }

    /** [Обязательный] Интервал значений для состояния: "Желтый" */
    public Collection<Float> getWarning() {
        return warning;
    }

    public void setWarning(Collection<Float> warning) {
        this.warning = warning;
    }

    /** [Обязательный] Интервал значений для состояния: "Оранжевый" */
    public Collection<Float> getCritical() {
        return critical;
    }

    public void setCritical(Collection<Float> critical) {
        this.critical = critical;
    }

    /** [Обязательный] Интервал значений для состояния: "Красный" */
    public Collection<Float> getFailed() {
        return failed;
    }

    public void setFailed(Collection<Float> failed) {
        this.failed = failed;
    }
}
