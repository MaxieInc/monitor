package ru.dtnm.monitor.rest.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.dtnm.monitor.CheckerContainer;
import ru.dtnm.monitor.history.HistoryHandler;
import ru.dtnm.monitor.model.config.alert.AlertConfigContainer;
import ru.dtnm.monitor.model.config.alert.AlertPerson;
import ru.dtnm.monitor.model.config.component.ComponentConfig;
import ru.dtnm.monitor.model.query.ComponentData;
import ru.dtnm.monitor.model.query.MonitoringResult;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Сервис: принимает сообщения от компонентов об их состоянии
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
@RestController
@RequestMapping("/accept")
public class ListenerService {

    private static final Logger LOG = LoggerFactory.getLogger(ListenerService.class);

    @Autowired
    private HistoryHandler historyHandler;

    @Autowired
    private CheckerContainer checkerContainer;

    /**
     * Рест-метод для приёма состояния компонента
     *
     * @param mnemo мнемо компонента
     * @param data данные о компоненте
     */
    @PostMapping(path = "/{mnemo}", consumes = MediaType.APPLICATION_JSON)
    public void accept(@PathVariable("mnemo") final String mnemo, @RequestBody final ComponentData data) throws IOException {
        LOG.debug(">> accept for mnemo={}", mnemo);
        if (mnemo == null || data == null) throw new BadRequestException("Both mnemo and data must not be null");

        final MonitoringResult monitoringResult = new MonitoringResult()
                .setMnemo(mnemo)
                .setLastOnline(new Date())
                .setComponentData(data);
        final ComponentConfig componentConfig = checkerContainer.getConfigByMnemo(mnemo);
        final AlertConfigContainer alertConfig = checkerContainer.getAlertConfigByMnemo(mnemo);

        historyHandler.handleQuery(
                monitoringResult,
                componentConfig,
                alertConfig.getAlerts().get(0),
                alertConfig.getTemplates(),
                alertConfig.getPersons().stream().collect(Collectors.toMap(AlertPerson::getLogin, e -> e)));
    }
}
