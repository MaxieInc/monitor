package ru.dtnm.monitor.checker;

import org.apache.http.HttpStatus;
import ru.dtnm.monitor.history.HistoryHandler;
import ru.dtnm.monitor.model.CheckResult;
import ru.dtnm.monitor.model.CheckStatus;

import java.util.Date;

/**
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public abstract class Checker {

    // Мнемоника опрашиваемого компонента
    protected String mnemo;
    // Урл опрашиваемого компонента
    protected String url;

    public String getMnemo() {
        return mnemo;
    }

    public String getUrl() {
        return url;
    }

    public abstract void check(HistoryHandler historyHandler);

    /**
     * Конструктор результата
     *
     * @param httpStatus статус ответа
     * @param start время начала опроса
     * @param end время окончания опроса
     */
    public CheckResult getResult(int httpStatus, final Date start, final Date end) {
        return new CheckResult()
                .withMnemo(mnemo)
                .withUrl(url)
                .withLastResponseDuration(end.getTime() - start.getTime())
                .withLastResponse(end)
                .withStatus(HttpStatus.SC_OK == httpStatus ? CheckStatus.UP : CheckStatus.DOWN);
    }

    public CheckResult getExceptionResult(final String comment) {
        return new CheckResult()
                .withMnemo(mnemo)
                .withUrl(url)
                .withStatus(CheckStatus.DOWN)
                .withComment(comment);
    }

    public Checker(final String mnemo, final String url) {
        this.mnemo = mnemo;
        this.url = url;
    }
}
