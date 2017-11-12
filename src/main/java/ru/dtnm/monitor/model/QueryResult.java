package ru.dtnm.monitor.model;

import org.apache.http.HttpStatus;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Транспорт: результат мониторинга
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class QueryResult implements Serializable {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private String mnemo;
    private String url;
    private Long lastResponseDuration;
    private Date lastResponse;
    private CheckStatus status;
    private String comment;

    public String getMnemo() {
        return mnemo;
    }

    public QueryResult withMnemo(String mnemo) {
        this.mnemo = mnemo;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public QueryResult withUrl(String url) {
        this.url = url;
        return this;
    }

    public Long getLastResponseDuration() {
        return lastResponseDuration;
    }

    public QueryResult withLastResponseDuration(Long lastResponseDuration) {
        this.lastResponseDuration = lastResponseDuration;
        return this;
    }

    public Date getLastResponse() {
        return lastResponse;
    }

    public String getLastResponseString() {
        return FORMAT.format(lastResponse);
    }

    public QueryResult withLastResponse(Date lastResponse) {
        this.lastResponse = lastResponse;
        return this;
    }

    public CheckStatus getStatus() {
        return status;
    }

    public QueryResult withStatus(CheckStatus status) {
        this.status = status;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public QueryResult withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String toString() {
        return String.format("[mnemo=%s, url=%s, status=%s]", mnemo, url, status);
    }

    /**
     * Конфтруктор по умолчанию
     */
    public QueryResult() {
    }

    /**
     * Конструктор результата
     *
     * @param httpStatus статус ответа
     * @param start время начала опроса
     * @param end время окончания опроса
     */
    public QueryResult(int httpStatus, final Date start, final Date end) {
        this.withLastResponseDuration(end.getTime() - start.getTime())
            .withLastResponse(end)
            // todo перенести логику заполнения статуса в @CheckResultBuilder
            .withStatus(HttpStatus.SC_OK == httpStatus ? CheckStatus.HEALTHY : CheckStatus.FAILED);
    }

    /**
     * Результат опроса в случае исключения
     *
     * @param mnemo мнемо опрашиваемого компонента
     * @param url УРЛ опрашиваемого компонента
     * @param comment комментарий (ошибка)
     */
    public QueryResult(final String mnemo, final String url, final String comment) {
        this.withMnemo(mnemo)
            .withUrl(url)
            // todo перенести логику заполнения статуса в @CheckResultBuilder
            .withStatus(CheckStatus.FAILED)
            .withComment(comment);
    }
}
