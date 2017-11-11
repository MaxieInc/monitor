package ru.dtnm.monitor.model.config;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>Конфиг: Компонент для мониторинга</p>
 * @author Яковлев В.Л.
 */
public class ComponentInfo implements Serializable {

    private String mnemo;
    private String caption;
    private String descr;
    private long keepAlive;
    private long timeout;
    private ComponentResponses responses;
    private Collection<ComponentMetric> metrics;
    private Collection<ComponentProperty> properties;

    /** [Обязательный] Строковый уникальный мнемокод компонента */
    public String getMnemo() {
        return mnemo;
    }

    public void setMnemo(String mnemo) {
        this.mnemo = mnemo;
    }

    /** [Обязательный] Короткий заголовок компонента (для отображения в веб-форме) */
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    /** [Необязательное] Подробное описание компонента (для всплывающих подсказок) */
    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    /** [Обязательный] Интервал (в миллисекундах), в течение которого компонент считается "живым" с момента последней активности */
    public long getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(long keepAlive) {
        this.keepAlive = keepAlive;
    }

    /** [Необязательное] Максимальное время ожидания (в миллисекундах) ответа от компонента (только для опрашиваемых компонентов) */
    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /** [Обязательное] Соответствие HTTP-ответов и статуса компонента */
    public ComponentResponses getResponses() {
        return responses;
    }

    public void setResponses(ComponentResponses responses) {
        this.responses = responses;
    }

    /** [Обязательные] Числовые метрики компонента */
    public Collection<ComponentMetric> getMetrics() {
        return metrics;
    }

    public void setMetrics(Collection<ComponentMetric> metrics) {
        this.metrics = metrics;
    }

    /** [Обязательные] Строковые свойства компонента */
    public Collection<ComponentProperty> getProperties() {
        return properties;
    }

    public void setProperties(Collection<ComponentProperty> properties) {
        this.properties = properties;
    }
}
