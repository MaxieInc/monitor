package ru.dtnm.monitor.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Транспорт: результат мониторинга
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class CheckResult {

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

    public CheckResult withMnemo(String mnemo) {
        this.mnemo = mnemo;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CheckResult withUrl(String url) {
        this.url = url;
        return this;
    }

    public Long getLastResponseDuration() {
        return lastResponseDuration;
    }

    public CheckResult withLastResponseDuration(Long lastResponseDuration) {
        this.lastResponseDuration = lastResponseDuration;
        return this;
    }

    public Date getLastResponse() {
        return lastResponse;
    }

    public String getLastResponseString() {
        return FORMAT.format(lastResponse);
    }

    public CheckResult withLastResponse(Date lastResponse) {
        this.lastResponse = lastResponse;
        return this;
    }

    public CheckStatus getStatus() {
        return status;
    }

    public CheckResult withStatus(CheckStatus status) {
        this.status = status;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public CheckResult withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String toString() {
        return String.format("[mnemo=%s, url=%s, status=%s]", mnemo, url, status);
    }
}
