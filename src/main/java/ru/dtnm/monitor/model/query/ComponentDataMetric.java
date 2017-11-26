package ru.dtnm.monitor.model.query;

import java.io.Serializable;

/**
 * Метрика в собственном ответе компонента
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class ComponentDataMetric implements Serializable {

    /** мнемо */
    private String mnemo;

    /** числовое значение */
    private Float value;

    public String getMnemo() {
        return mnemo;
    }

    public void setMnemo(String mnemo) {
        this.mnemo = mnemo;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    /**
     * Вспомогательный: проверяет, принадлежит лди число интеравлу
     *
     * @param interval интервал (открытый справа)
     */
    public boolean inInterval(final Float[] interval) {
        if (interval == null || interval[0] == null || interval[1] == null) return false;
        else return (interval[0] <= value && value <= interval[1]);
    }
}
