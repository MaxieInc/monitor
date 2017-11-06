package ru.dtnm.monitor.checker;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import ru.dtnm.monitor.history.HistoryHandler;
import ru.dtnm.monitor.model.CheckResult;
import ru.dtnm.monitor.model.CheckStatus;

import java.io.IOException;
import java.util.Date;


/**
 * Простой компонент, создаваемый по умолчанию
 * Конструируется из JSONа
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class SimpleChecker extends Checker {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleChecker.class);

    public void check (final HistoryHandler historyHandler) {
        final HttpClient httpClient = HttpClients.createDefault();
        Integer httpStatus = null;
        HttpResponse response = null;
        final Date startDate = new Date();
        Date endDate = null;
        try {
            final HttpGet get = new HttpGet(url);
            response = httpClient.execute(get);
            endDate = new Date();

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                LOG.debug("Success");
            } else {
                LOG.debug("Error! Status is {}", response.getStatusLine().getStatusCode());
            }
            final CheckResult result = getResult(response.getStatusLine().getStatusCode(), startDate, endDate);

            historyHandler.writeHistory(mnemo, result);
        } catch (IOException ioe) {
            LOG.error("Unable to perform check: {}", ioe.getMessage(), ioe);
            endDate = new Date();
        }
    }

    public CheckResult getResult(int httpStatus, final Date start, final Date end) {
        return new CheckResult()
                .withMnemo(mnemo)
                .withUrl(url)
                .withLastResponseDuration(end.getTime() - start.getTime())
                .withLastResponse(end)
                .withStatus(HttpStatus.SC_OK == httpStatus ? CheckStatus.UP : CheckStatus.DOWN);
    }

    private SimpleChecker() {}

    public SimpleChecker(String mnemo, String url) {
        this.mnemo = mnemo;
        this.url = url;
    }


}
