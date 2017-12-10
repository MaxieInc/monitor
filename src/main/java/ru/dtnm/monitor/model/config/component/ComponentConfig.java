package ru.dtnm.monitor.model.config.component;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>Конфиг: Компонент для мониторинга</p>
 * @author Яковлев В.Л.
 */
public class ComponentConfig implements Serializable {

    private String mnemo;
    private String caption;
    private String descr;
    private String url;
    private long keepAlive;
    private Integer timeout;
    private ComponentQueryType type;
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

    /** [Обязательное] УРЛ опрашиваемого компонета*/
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /** [Обязательный] Интервал (в миллисекундах), в течение которого компонент считается "живым" с момента последней активности */
    public long getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(long keepAlive) {
        this.keepAlive = keepAlive;
    }

    /** [Необязательное] Максимальное время ожидания (в миллисекундах) ответа от компонента (только для опрашиваемых компонентов) */
    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    /** Обязательное: тип получения данных о компоненте*/
    public ComponentQueryType getType() {
        return type;
    }

    public void setType(ComponentQueryType type) {
        this.type = type;
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
