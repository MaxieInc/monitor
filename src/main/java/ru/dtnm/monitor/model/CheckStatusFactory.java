package ru.dtnm.monitor.model;

import ru.dtnm.monitor.model.config.component.ComponentConfig;
import ru.dtnm.monitor.model.config.component.ComponentMetric;
import ru.dtnm.monitor.model.config.component.ComponentResponses;
import ru.dtnm.monitor.model.config.component.PropMnemoConstant;
import ru.dtnm.monitor.model.query.ComponentDataMetric;
import ru.dtnm.monitor.model.query.MonitoringResult;
import ru.dtnm.monitor.model.status.CheckMnemoConstant;
import ru.dtnm.monitor.model.status.CheckStatus;
import ru.dtnm.monitor.model.status.CheckStatusContainer;
import ru.dtnm.monitor.model.status.CheckStatusResponse;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Сопоставление результатов опроса компонента с данными из конфига
 * для формирования решения о работоспособности компонента
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class CheckStatusFactory {

    /**
     * Формирует ответ о работоспособности компонента по резальтутам последнего опроса
     *
     * @param monitoringResult результат последнего опроса компонента
     * @param componentConfig конфигурация наблюдаемого компонента
     */
    public static CheckStatusResponse status(final MonitoringResult monitoringResult, final ComponentConfig componentConfig) throws IOException {
        final CheckStatusResponse response = new CheckStatusResponse();
        final List<CheckStatusContainer> statuses = new ArrayList<>();

        // 1. Проверка на соответствие HTTP - статуса ответа и конфига
        statuses.add(checkStatusMasks(monitoringResult.getHttpStatus(), componentConfig.getResponses()));

        // 2. Проверка на длительность ответа
        statuses.add(checkCallDuration(monitoringResult.getComponentData().getMetrics(), componentConfig.getMetrics()));

        // 3. Проверка по KeepAlive
        final Date now = new Date();
        final CheckStatus keepAliveStatus = now.getTime() - monitoringResult.getLastOnline().getTime() > componentConfig.getKeepAlive()
                ? CheckStatus.FAILED
                : CheckStatus.HEALTHY;
        statuses.add(new CheckStatusContainer(keepAliveStatus, "KeepAlive check"));

        // 4. Проверка по числовым метрикам
        if (monitoringResult.getComponentData() != null && monitoringResult.getComponentData().getMetrics() != null) {
            statuses.addAll(checkMetrics(componentConfig.getMetrics(), monitoringResult.getComponentData().getMetrics()));
        }

        response.setLastResponse(monitoringResult);
        final CheckStatusContainer resultStatusContainer = statuses
                .stream()
                .reduce(CheckStatusContainer::getWorst)
                .orElse(new CheckStatusContainer(CheckStatus.UNKNOWN, CheckMnemoConstant.UNKNOWN));
        response.setStatus(resultStatusContainer.getStatus())
                .setReason(resultStatusContainer.getReason());
        return response;
    }

    /**
     * Проверка статуса по маске тела ответа
     *
     * @param status тело ответа
     * @param componentResponses описание масок ответов для статусов
     */
    private static CheckStatusContainer checkStatusMasks(final Integer status, final ComponentResponses componentResponses) {
        final CheckStatusContainer result = new CheckStatusContainer().setReason(CheckMnemoConstant.HTTP_STATUS_CHECK);
        if (status == null) { // статуса нет - не получили ответа за указанное время, обработка таймаута
            result.setStatus(CheckStatus.valueOf(componentResponses.getTimeout()));
        } else if (matches(status, componentResponses.getHealthy())) result.setStatus(CheckStatus.HEALTHY);
        else if (matches(status, componentResponses.getWarning())) result.setStatus(CheckStatus.WARNING);
        else if (matches(status, componentResponses.getCritical())) result.setStatus(CheckStatus.CRITICAL);
        else if (matches(status, componentResponses.getFailed())) result.setStatus(CheckStatus.FAILED);
        else result.setStatus(CheckStatus.valueOf(componentResponses.getOthers()));

        return result;
    }

    /**
     * Проверяет по длительности ответа (метрика с известным мнемо)
     * @param realMetrics реальные метрики
     * @param configMetrics конфигурационные метрики
     */
    private static CheckStatusContainer checkCallDuration(final Collection<ComponentDataMetric> realMetrics, final Collection<ComponentMetric> configMetrics) {
        final CheckStatusContainer result = new CheckStatusContainer()
                .setStatus(CheckStatus.UNKNOWN)
                .setReason(CheckMnemoConstant.CALL_DURATION_CHECK);
        final ComponentDataMetric realMetric = realMetrics
                .stream()
                .filter(e -> PropMnemoConstant.CALL_DURATION_MNEMO.equals(e.getMnemo()))
                .findFirst()
                .orElse(null);
        final ComponentMetric configMetric = configMetrics
                .stream()
                .filter(m -> PropMnemoConstant.CALL_DURATION_MNEMO.equals(m.getMnemo()))
                .findFirst()
                .orElse(null);

        if (realMetric == null || configMetric == null) result.setStatus(CheckStatus.UNKNOWN);
        else if (realMetric.inInterval(configMetric.getHealthy())) result.setStatus(CheckStatus.HEALTHY);
        else if (realMetric.inInterval(configMetric.getWarning())) result.setStatus(CheckStatus.WARNING);
        else if (realMetric.inInterval(configMetric.getCritical())) result.setStatus(CheckStatus.CRITICAL);
        else if (realMetric.inInterval(configMetric.getFailed())) result.setStatus(CheckStatus.FAILED);

        return result;
    }

    /**
     * Проверяет, соответствует ли статус набору шаблонов
     *
     * @param status статус
     * @param patterns шаблоны
     */
    private static boolean matches(final Integer status, final Collection<String> patterns) {
        if (status == null) return false;
        final String statusString = String.valueOf(status);
        return patterns
                .stream()
                .map(e -> Pattern.compile(e).matcher(statusString).matches())
                .filter(e -> e.equals(true))
                .findFirst()
                .orElse(false);
    }

    /**
     * Сравнивает метрики реального ответа с метриками, которым должен соответствовать компонент
     *
     * @param configMetrics метрики, которым должен соответствовать компонент
     * @param realMetrics реальные метрики компонента
     */
    private static List<CheckStatusContainer> checkMetrics(final Collection<ComponentMetric> configMetrics,  final Collection<ComponentDataMetric> realMetrics) {
        final List<CheckStatus> statuses = new ArrayList<>();
        final Map<String, ComponentMetric> configMetricsMap = configMetrics.stream().collect(Collectors.toMap(ComponentMetric::getMnemo, e -> e));
        if (realMetrics != null) {
            for (ComponentDataMetric realMetric : realMetrics) {
                final ComponentMetric configMetric = configMetricsMap.get(realMetric.getMnemo());
                if (configMetric != null) {
                    if (realMetric.inInterval(configMetric.getHealthy())) statuses.add(CheckStatus.HEALTHY);
                    else if (realMetric.inInterval(configMetric.getWarning())) statuses.add(CheckStatus.WARNING);
                    else if (realMetric.inInterval(configMetric.getCritical())) statuses.add(CheckStatus.CRITICAL);
                    else if (realMetric.inInterval(configMetric.getFailed())) statuses.add(CheckStatus.FAILED);
                } else statuses.add(CheckStatus.UNKNOWN);
            }
        }
        return statuses
                .stream()
                .map(e -> new CheckStatusContainer(e, CheckMnemoConstant.METRICS_CHECK))
                .collect(Collectors.toList());
    }
}
