package ru.dtnm.monitor.model.query;

import ru.dtnm.monitor.model.config.component.ComponentProperty;

import java.util.Collection;

/**
 * Транспорт: данные, сообщаемые о себе самим компонентом
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class ComponentData {

    /** Набор метрик компонента */
    private Collection<ComponentDataMetric> metrics;

    /** Набор строковых характеристик */
    private Collection<ComponentProperty> properties;


    public Collection<ComponentDataMetric> getMetrics() {
        return metrics;
    }

    public ComponentData setMetrics(Collection<ComponentDataMetric> metrics) {
        this.metrics = metrics;
        return this;
    }

    public Collection<ComponentProperty> getProperties() {
        return properties;
    }

    public ComponentData setProperties(Collection<ComponentProperty> properties) {
        this.properties = properties;
        return this;
    }
}
