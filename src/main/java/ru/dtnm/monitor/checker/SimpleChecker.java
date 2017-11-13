package ru.dtnm.monitor.checker;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dtnm.monitor.history.HistoryHandler;
import ru.dtnm.monitor.model.query.ComponentResponse;
import ru.dtnm.monitor.model.config.alert.AlertConfig;
import ru.dtnm.monitor.model.config.component.ComponentInfo;
import ru.dtnm.monitor.notification.AlertHandler;

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

    /**
     * Опрос интересующего компонента
     *
     * @param historyHandler
     */
    public void check (final HistoryHandler historyHandler, final AlertHandler alertHandler) {
        final ComponentResponse componentResponse = new ComponentResponse()
                .setMnemo(componentInfo.getMnemo())
                .setUrl(componentInfo.getUrl());
        final HttpClient httpClient = getClient();
        final Date startDate = new Date();
        Date endDate = null;
        try {
            final HttpGet get = new HttpGet(componentInfo.getUrl());
            final HttpResponse response = httpClient.execute(get);
            endDate = new Date();
            componentResponse.setLastOnline(endDate);

            componentResponse
                    .setHttpStatus(response.getStatusLine().getStatusCode())
                    .setResponseDuration(endDate.getTime() - startDate.getTime());
            historyHandler.writeHistory(componentResponse);
        } catch (IOException ioe) {
            LOG.error("Unable to perform check: {}", ioe.getMessage(), ioe);
            historyHandler.writeHistory(componentResponse.setComment(ioe.getMessage()));
        }
    }

    public SimpleChecker(final ComponentInfo componentInfo, final AlertConfig alertConfig) {
        super(componentInfo, alertConfig);
    }

    /**
     * Конструирует Http-клиент
     */
    private HttpClient getClient() {
        if (componentInfo.getTimeout() != null) {
            final RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(componentInfo.getTimeout())
                    .setConnectTimeout(componentInfo.getTimeout())
                    .setConnectionRequestTimeout(componentInfo.getTimeout())
                    .build();
            return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        } else return HttpClients.createDefault();
    }
}
