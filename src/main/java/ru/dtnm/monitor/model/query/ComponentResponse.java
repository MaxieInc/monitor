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
public class ComponentResponse implements Serializable {

    private String mnemo;
    private String url;
    private Integer httpStatus;
    private Long responseDuration;
    private Date lastOnline;
    private String comment;

    public String getMnemo() {
        return mnemo;
    }

    public ComponentResponse setMnemo(String mnemo) {
        this.mnemo = mnemo;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ComponentResponse setUrl(String url) {
        this.url = url;
        return this;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public ComponentResponse setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public Long getResponseDuration() {
        return responseDuration;
    }

    public ComponentResponse setResponseDuration(Long responseDuration) {
        this.responseDuration = responseDuration;
        return this;
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public ComponentResponse setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public ComponentResponse setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String toString() {
        return String.format("[mnemo=%s, url=%s, httpStatus=%s]", mnemo, url, httpStatus);
    }

    /**
     * Конcтруктор по умолчанию
     */
    public ComponentResponse() {
    }
}
