package ru.dtnm.monitor.model;

import ru.dtnm.monitor.model.config.component.ComponentInfo;
import ru.dtnm.monitor.model.config.component.ComponentResponses;
import ru.dtnm.monitor.model.query.ComponentResponse;
import ru.dtnm.monitor.model.status.CheckStatus;
import ru.dtnm.monitor.model.status.CheckStatusResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
     * @param componentResponse результат последнего опроса компонента
     * @param componentInfo конфигурация наблюдаемого компонента
     */
    public static CheckStatusResponse status(final ComponentResponse componentResponse, final ComponentInfo componentInfo) throws IOException {
        final CheckStatusResponse response = new CheckStatusResponse();
        final List<CheckStatus> statuses = new ArrayList<>();
        // 1. Проверка на соответствие тела HTTP-ответа и статуса компонента
        statuses.add(checkResponseStrings("200 ok", componentInfo.getResponses()));
        // 2. Проверка по KeepAlive
        final Date now = new Date();
        statuses.add(now.getTime() - componentResponse.getLastOnline().getTime() < componentInfo.getKeepAlive()
                ? CheckStatus.FAILED
                : CheckStatus.HEALTHY);
        // 3. Проверка на таймаут
        // todo Реализовать
        // 4. Проверка по числовым метрикам
        // todo Реализовать
        // 5. Проверка по пропертям
        // todo Реализовать
        response.setLastResponse(componentResponse);
        response.setStatus(statuses.stream().reduce(CheckStatus::getWorst).get());
        return response;
    }

    /**
     * Проверка статуса по маске тела ответа
     *
     * @param responseString тело ответа
     * @param componentResponses описание масок ответов для статусов
     */
    private static CheckStatus checkResponseStrings(final String responseString, final ComponentResponses componentResponses) {
        // todo реализовать поиск по регулярному выражению
        if (componentResponses.getHealthy().contains(responseString)) return CheckStatus.HEALTHY;
        else if (componentResponses.getWarning().contains(responseString)) return CheckStatus.WARNING;
        else if (componentResponses.getCritical().contains(responseString)) return CheckStatus.CRITICAL;
        else if (componentResponses.getFailed().contains(responseString)) return CheckStatus.FAILED;
        else if (responseString.toLowerCase().contains("timeout")) return CheckStatus.valueOf(componentResponses.getTimeout());
        else return CheckStatus.valueOf(componentResponses.getOthers());
    }
}
