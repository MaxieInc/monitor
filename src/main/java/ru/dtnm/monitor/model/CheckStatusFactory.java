package ru.dtnm.monitor.model;

import ru.dtnm.monitor.model.config.component.ComponentConfig;
import ru.dtnm.monitor.model.config.component.ComponentMetric;
import ru.dtnm.monitor.model.config.component.ComponentProperty;
import ru.dtnm.monitor.model.config.component.ComponentResponses;
import ru.dtnm.monitor.model.query.ComponentDataMetric;
import ru.dtnm.monitor.model.query.MonitoringResult;
import ru.dtnm.monitor.model.status.CheckStatus;
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
        final List<CheckStatus> statuses = new ArrayList<>();
        // 1. Проверка на соответствие HTTP - статуса ответа и конфига, и таймаут
        statuses.add(checkResponseStrings(monitoringResult.getHttpStatus(), componentConfig.getResponses()));
        // 2. Проверка по KeepAlive
        final Date now = new Date();
        statuses.add(now.getTime() - monitoringResult.getLastOnline().getTime() > componentConfig.getKeepAlive()
                ? CheckStatus.FAILED
                : CheckStatus.HEALTHY);

        if (monitoringResult.getComponentData() != null) {
            // 3. Проверка по числовым метрикам
            statuses.addAll(checkMetrics(componentConfig.getMetrics(), monitoringResult.getComponentData().getMetrics()));
            // 4. Проверка по строковым свойствам
            statuses.addAll(checkProperties(componentConfig.getProperties(), monitoringResult.getComponentData().getProperties()));
        }

        response.setLastResponse(monitoringResult);
        response.setStatus(statuses.stream().reduce(CheckStatus::getWorst).orElse(CheckStatus.UNKNOWN));
        return response;
    }

    /**
     * Проверка статуса по маске тела ответа
     *
     * @param status тело ответа
     * @param componentResponses описание масок ответов для статусов
     */
    private static CheckStatus checkResponseStrings(final Integer status, final ComponentResponses componentResponses) {
        if (status == null) { // статуса нет - не получили ответа за указанное время, обработка таймаута
            return CheckStatus.valueOf(componentResponses.getTimeout());
        } else if (matches(status, componentResponses.getHealthy())) return CheckStatus.HEALTHY;
        else if (matches(status, componentResponses.getWarning())) return CheckStatus.WARNING;
        else if (matches(status, componentResponses.getCritical())) return CheckStatus.CRITICAL;
        else if (matches(status, componentResponses.getFailed())) return CheckStatus.FAILED;
        else return CheckStatus.valueOf(componentResponses.getOthers());
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
                .filter(e -> true)
                .findFirst()
                .orElse(false);
    }

    /**
     * Сравнивает метрики реального ответа с метриками, которым должен соответствовать компонент
     *
     * @param configMetrics метрики, которым должен соответствовать компонент
     * @param realMetrics реальные метрики компонента
     */
    private static List<CheckStatus> checkMetrics(final Collection<ComponentMetric> configMetrics,  final Collection<ComponentDataMetric> realMetrics) {
        final List<CheckStatus> statuses = new ArrayList<>();
        final Map<String, ComponentMetric> configMetricsMap = configMetrics.stream().collect(Collectors.toMap(ComponentMetric::getMnemo, e -> e));
        for (ComponentDataMetric realMetric : realMetrics) {
            final ComponentMetric configMetric = configMetricsMap.get(realMetric.getMnemo());
            if (configMetric != null) {
                if (realMetric.inInterval(configMetric.getHealthy())) statuses.add(CheckStatus.HEALTHY);
                else if (realMetric.inInterval(configMetric.getWarning())) statuses.add(CheckStatus.WARNING);
                else if (realMetric.inInterval(configMetric.getCritical())) statuses.add(CheckStatus.CRITICAL);
                else if (realMetric.inInterval(configMetric.getFailed())) statuses.add(CheckStatus.FAILED);
            } else statuses.add(CheckStatus.UNKNOWN);
        }
        return statuses;
    }

    /**
     * Сравнивает строковые свойства ответа и конфига
     *
     * @param configProperties конфигурация строковых свойств
     * @param realProperties реальные строковые свойства из ответа компонента
     */
    private static List<CheckStatus> checkProperties(final Collection<ComponentProperty> configProperties, final Collection<ComponentProperty> realProperties) {
        final List<CheckStatus> statuses = new ArrayList<>();
        final Map<String, ComponentProperty> realPropsMap = realProperties.stream().collect(Collectors.toMap(ComponentProperty::getMnemo, e -> e));
        for (ComponentProperty configProperty : configProperties) {
            ComponentProperty realProperty = realPropsMap.remove(configProperty.getMnemo());
            if (realProperty == null) {
                statuses.add(configProperty.isMandatory() ? CheckStatus.FAILED : CheckStatus.WARNING);
            } else if (configProperty.getValue() != null) {
                statuses.add(configProperty.getValue().equals(realProperty.getValue()) ? CheckStatus.HEALTHY : CheckStatus.WARNING);
            }
        }
        if (realPropsMap.keySet().size() > 0) {
            statuses.add(CheckStatus.UNKNOWN);
        }
        return statuses;
    }
}
