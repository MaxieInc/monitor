package ru.dtnm.monitor.model;

import ru.dtnm.monitor.model.config.component.*;
import ru.dtnm.monitor.model.query.ComponentDataMetric;
import ru.dtnm.monitor.model.query.MonitoringResult;
import ru.dtnm.monitor.model.status.CheckMnemoConstant;
import ru.dtnm.monitor.model.status.CheckStatus;
import ru.dtnm.monitor.model.status.CheckStatusContainer;
import ru.dtnm.monitor.model.status.CheckStatusResponse;

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
    public static CheckStatusResponse status(final MonitoringResult monitoringResult, final ComponentConfig componentConfig) {
        final CheckStatusResponse response = new CheckStatusResponse();
        final List<CheckStatusContainer> statuses = new ArrayList<>();

        // 1. Проверка на соответствие HTTP - статуса ответа и конфига
        statuses.add(checkStatusMasks(monitoringResult.getHttpStatus(), componentConfig.getResponses()));

        // 2. Проверка по KeepAlive
        final Date now = new Date();
        CheckStatus keepAliveStatus = CheckStatus.UNKNOWN;
        if (monitoringResult.getLastOnline() != null) {
            keepAliveStatus = now.getTime() - monitoringResult.getLastOnline().getTime() > componentConfig.getKeepAlive()
                    ? CheckStatus.FAILED
                    : CheckStatus.HEALTHY;
        }
        statuses.add(new CheckStatusContainer(keepAliveStatus, CheckMnemoConstant.KEEP_ALIVE_CHECK));

        // 3. Проверка по числовым метрикам
        statuses.addAll(checkMetrics(componentConfig.getMetrics(), monitoringResult.getComponentData().getMetrics()));

        // 4. Проверка по строковым метрикам
        statuses.addAll(checkProperties(componentConfig.getProperties(), monitoringResult.getComponentData().getProperties()));

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
        final List<CheckStatus> result = new ArrayList<>();
        final Map<String, ComponentDataMetric> realMetricsMap = realMetrics
                .stream()
                .collect(Collectors.toMap(ComponentDataMetric::getMnemo, e -> e));
        for (ComponentMetric configMetric : configMetrics) {
            final ComponentDataMetric realMetric = realMetricsMap.get(configMetric.getMnemo());

            if (configMetric.isMandatory() && realMetric == null) {
                // Если нет обязательной метрики - критикал
                result.add(CheckStatus.CRITICAL);
            } else if (realMetric != null) {
                // Если есть с чем сравнивать - сравниваем
                if (realMetric.inInterval(configMetric.getHealthy())) result.add(CheckStatus.HEALTHY);
                else if (realMetric.inInterval(configMetric.getWarning())) result.add(CheckStatus.WARNING);
                else if (realMetric.inInterval(configMetric.getCritical())) result.add(CheckStatus.CRITICAL);
                else if (realMetric.inInterval(configMetric.getFailed())) result.add(CheckStatus.FAILED);
                else result.add(CheckStatus.UNKNOWN);
            }
        }
        return result
                .stream()
                .map(e -> new CheckStatusContainer(e, CheckMnemoConstant.METRICS_CHECK))
                .collect(Collectors.toList());
    }


    /**
     * Проверяет совпадение строковых пропертей
     *
     * @param configProperties проперти из конфига
     * @param realProperties проперти из ответа компонента
     */
    private static List<CheckStatusContainer> checkProperties(final Collection<ComponentProperty> configProperties, final Collection<ComponentProperty> realProperties) {
        final List<CheckStatus> result = new ArrayList<>();
        final Map<String, ComponentProperty> realPropertiesMap = realProperties
                .stream()
                .collect(Collectors.toMap(ComponentProperty::getMnemo, e -> e));

        for (ComponentProperty configProperty : configProperties) {
            final ComponentProperty realProperty = realPropertiesMap.get(configProperty.getMnemo());

            if (configProperty.isMandatory() && realProperty == null) {
                // Если нет обязательной проперти - критикал
                result.add(CheckStatus.CRITICAL);
            } else if (realProperty != null) {
                // если строковые значения совпадают - нормально
                if (realProperty.getValue().equals(configProperty.getValue())) result.add(CheckStatus.HEALTHY);
                // если не совпадают - просто варнинг
                else result.add(CheckStatus.WARNING);
            }
        }
        return result
                .stream()
                .map(e -> new CheckStatusContainer(e, CheckMnemoConstant.PROPERTIES_CHECK))
                .collect(Collectors.toList());
    }
}
