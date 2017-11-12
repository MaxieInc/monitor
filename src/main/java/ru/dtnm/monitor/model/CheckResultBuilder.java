package ru.dtnm.monitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.dtnm.monitor.CheckerContainer;
import ru.dtnm.monitor.model.config.component.ComponentInfo;

import java.io.IOException;

/**
 * Сопоставление результатов опроса компонента с данными из конфига
 * для формирования решения о работоспособности компонента
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
@Component
public class CheckResultBuilder {

    private static Logger LOG = LoggerFactory.getLogger(CheckResultBuilder.class);

    @Autowired
    private CheckerContainer checkerContainer;

    /**
     * Формирует ответ о работоспособности компонента по резальтутам последнего опроса
     *
     * @param queryResult результат последнего опроса компонента
     */
    public CheckResult getResult(final QueryResult queryResult) throws IOException {
        LOG.debug(">> getResult");
        final CheckResult result = new CheckResult();
        final ComponentInfo config = checkerContainer.getConfigByMnemo(queryResult.getMnemo());
        // todo тут реализовать логику разбора ответа
        result.setStatus(queryResult.getStatus());
        // todo тут реализовать логику разбора ответа
        return result;
    }
}
