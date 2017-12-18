package ru.dtnm.monitor.checker;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dtnm.monitor.history.HistoryHandler;
import ru.dtnm.monitor.model.config.component.ComponentConfig;
import ru.dtnm.monitor.model.config.component.PropMnemoConstant;
import ru.dtnm.monitor.model.query.ComponentData;
import ru.dtnm.monitor.model.query.ComponentDataMetric;
import ru.dtnm.monitor.model.query.MonitoringResult;
import ru.dtnm.monitor.model.config.alert.AlertConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    public void check (final HistoryHandler historyHandler) {
        final MonitoringResult monitoringResult = new MonitoringResult()
                .setMnemo(this.componentConfig.getMnemo())
                .setUrl(this.componentConfig.getUrl());
        final HttpClient httpClient = getClient();
        final Date startDate = new Date();
        Date endDate = null;
        ComponentData componentData = new ComponentData()
                .setProperties(new ArrayList<>())
                .setMetrics(new ArrayList<>());
        try (CloseableHttpClient client = getClient()) {
            final HttpGet get = new HttpGet(this.componentConfig.getUrl());
            final HttpResponse response = client.execute(get);
            endDate = new Date();
            try {
                final String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                LOG.debug("response is: {}", responseString);
                final ComponentData recieved = MAPPER.readValue(responseString, ComponentData.class);
                if (recieved.getMetrics() != null) {
                    componentData.getMetrics().addAll(recieved.getMetrics());
                }
                if (recieved.getProperties() != null) {
                    componentData.getProperties().addAll(recieved.getProperties());
                }
            } catch (Exception e) {
                LOG.error("Unable to parse ComponentData!");
            }
            monitoringResult
                    .setLastOnline(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
                            ? endDate
                            : monitoringResult.getLastOnline())
                    .setComponentData(componentData)
                    .setHttpStatus(response.getStatusLine().getStatusCode());

            // Положим известную нам числовую проперть - длительность вызова
            monitoringResult.getComponentData().getMetrics().add(new ComponentDataMetric(PropMnemoConstant.CALL_DURATION_MNEMO, (float) (endDate.getTime() - startDate.getTime())));

            historyHandler.handleQuery(monitoringResult, this.componentConfig, alertConfig);
        } catch (IOException ioe) {
            LOG.error("Unable to perform check: {}", ioe.toString());
            historyHandler.handleQuery(monitoringResult.setComment(ioe.getMessage()), this.componentConfig, alertConfig);
        }
    }

    public SimpleChecker(final ComponentConfig componentConfig, final AlertConfig alertConfig) {
        super(componentConfig, alertConfig);
    }

    /**
     * Конструирует Http-клиент
     */
    private CloseableHttpClient getClient() {
        if (componentConfig.getTimeout() != null) {
            final RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(componentConfig.getTimeout())
                    .setConnectTimeout(componentConfig.getTimeout())
                    .setConnectionRequestTimeout(componentConfig.getTimeout())
                    .build();
            return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        } else return HttpClients.createDefault();
    }
}
