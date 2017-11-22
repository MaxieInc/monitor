package ru.dtnm.monitor.model.config.component;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>Конфиг: Соответствие HTTP-ответа и статуса компонента</p>
 * @author Яковлев В.Л.
 */
public class ComponentResponses implements Serializable {

    private Collection<String> healthy;
    private Collection<String> warning;
    private Collection<String> critical;
    private Collection<String> failed;
    private String others;
    private String timeout;

    /** [Обязательные] Строковые маски HTTP-статусов ответов, соответствующие статусу компонента: Зеленый */
    public Collection<String> getHealthy() {
        return healthy;
    }

    public void setHealthy(Collection<String> healthy) {
        this.healthy = healthy;
    }

    /** [Обязательные] Строковые маски HTTP-ответов, соответствующие статусу компонента: Желтый */
    public Collection<String> getWarning() {
        return warning;
    }

    public void setWarning(Collection<String> warning) {
        this.warning = warning;
    }

    /** [Обязательные] Строковые маски HTTP-ответов, соответствующие статусу компонента: Оранжевый */
    public Collection<String> getCritical() {
        return critical;
    }

    public void setCritical(Collection<String> critical) {
        this.critical = critical;
    }

    /** [Обязательные] Строковые маски HTTP-ответов, соответствующие статусу компонента: Красный */
    public Collection<String> getFailed() {
        return failed;
    }

    public void setFailed(Collection<String> failed) {
        this.failed = failed;
    }

    /** [Обязательный] Статус компонента (зеленый/.../красный) для всех не перечисленных выше HTTP-ответов */
    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    /** [Обязательный] Статус компонента (зеленый/.../красный) при отсутствии HTTP-ответа (по тайм-ауту) */
    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
