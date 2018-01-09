package ru.dtnm.monitor.model.config.component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

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

    public void setHealthy(String[] healthy) {
        this.healthy = fillFloatArray(healthy);
    }

    /** [Обязательный] Интервал значений для состояния: "Желтый" */
    public Float[] getWarning() {
        return warning;
    }

    public void setWarning(String[] warning) {
        this.warning = fillFloatArray(warning);
    }

    /** [Обязательный] Интервал значений для состояния: "Оранжевый" */
    public Float[] getCritical() {
        return critical;
    }

    public void setCritical(String[] critical) {
        this.critical = fillFloatArray(critical);
    }

    /** [Обязательный] Интервал значений для состояния: "Красный" */
    public Float[] getFailed() {
        return failed;
    }

    public void setFailed(String[] failed) {
        this.failed = fillFloatArray(failed);
    }


    /**
     * Вспомогательный: Заполняет массив значений из массива строк
     *
     * @param values строки - значения
     */
    private static Float[] fillFloatArray(final String[] values) {
        final List<String> valuesList = Arrays.asList(values);
        final Float[] result = new Float[2];
        valuesList.forEach(e -> {
            int index = valuesList.indexOf(e);
            if (e.equals("MAX")) {
                result[index] = Float.MAX_VALUE;
            } else result[index] = Float.parseFloat(e);
        });
        return result;
    }
}
