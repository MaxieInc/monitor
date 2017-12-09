package ru.dtnm.monitor.model.config.component;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>Конфиг: Числовая метрика компонента для мониторинга</p>
 * @author Яковлев В.Л.
 */
public class ComponentMetric implements Serializable {

    private String mnemo;
    private boolean mandatory;
    private Float[] healthy;
    private Float[] warning;
    private Float[] critical;
    private Float[] failed;

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
    public Float[] getHealthy() {
        return healthy;
    }

    public void setHealthy(Float[] healthy) {
        this.healthy = healthy;
    }

    /** [Обязательный] Интервал значений для состояния: "Желтый" */
    public Float[] getWarning() {
        return warning;
    }

    public void setWarning(Float[] warning) {
        this.warning = warning;
    }

    /** [Обязательный] Интервал значений для состояния: "Оранжевый" */
    public Float[] getCritical() {
        return critical;
    }

    public void setCritical(Float[] critical) {
        this.critical = critical;
    }

    /** [Обязательный] Интервал значений для состояния: "Красный" */
    public Float[] getFailed() {
        return failed;
    }

    public void setFailed(Float[] failed) {
        this.failed = failed;
    }
}
