package ru.dtnm.monitor.model.query;

import java.io.Serializable;
import java.util.Date;

/**
 * Транспорт: результат мониторинга
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class MonitoringResult implements Serializable {

    /** Мнемоника компонента */
    private String mnemo;

    /** УРЛ опрашиваемого компонента */
    private String url;

    /** Статус опроса компонента */
    private Integer httpStatus;

    /** Длительность опроса */
    private Long responseDuration;

    /** Последняя связь с компонентом */
    private Date lastOnline;

    /** Комментарий */
    private String comment;

    /** Данные в ответе компонента */
    private ComponentData componentData;


    public String getMnemo() {
        return mnemo;
    }

    public MonitoringResult setMnemo(String mnemo) {
        this.mnemo = mnemo;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public MonitoringResult setUrl(String url) {
        this.url = url;
        return this;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public MonitoringResult setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public Long getResponseDuration() {
        return responseDuration;
    }

    public MonitoringResult setResponseDuration(Long responseDuration) {
        this.responseDuration = responseDuration;
        return this;
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public MonitoringResult setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public MonitoringResult setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public ComponentData getComponentData() {
        return componentData;
    }

    public MonitoringResult setComponentData(ComponentData componentData) {
        this.componentData = componentData;
        return this;
    }

    public String toString() {
        return String.format("[mnemo=%s, url=%s, httpStatus=%s]", mnemo, url, httpStatus);
    }

    /**
     * Конcтруктор по умолчанию
     */
    public MonitoringResult() {
    }
}
