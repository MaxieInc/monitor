package ru.dtnm.monitor.rest.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dtnm.monitor.CheckerContainer;
import ru.dtnm.monitor.history.HistoryHandler;
import ru.dtnm.monitor.model.CheckResult;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
@RestController
@RequestMapping("/info")
public class MonitorService {

    private static final Logger LOG = LoggerFactory.getLogger(MonitorService.class);

    @Autowired
    private HistoryHandler historyHandler;

    @Autowired
    private CheckerContainer checkerContainer;

    /**
     * Возвращает результат последнего опроса компонента по его мнемо
     * @param mnemo мнемо компонента
     */
    @GetMapping(path = "/{mnemo}", produces = MediaType.APPLICATION_JSON)
    public CheckResult getStatus(@PathVariable("mnemo") final String mnemo) {
        LOG.debug(">> getStatus by mnemo: {}", mnemo);
        return historyHandler.getLastCheckResult(mnemo);
    }

    /**
     * Возвращает список результатов опросов всех зарегистрированных компонентов
     */
    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON)
    public List<CheckResult> getAllResults() {
        LOG.debug(">> getAllResults");
        return checkerContainer
                .getRegisteredMnemos()
                .stream()
                .map(e -> historyHandler.getLastCheckResult(e))
                .collect(Collectors.toList());
    }
}
