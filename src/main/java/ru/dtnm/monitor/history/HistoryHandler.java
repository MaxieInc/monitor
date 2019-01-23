package ru.dtnm.monitor.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.helpers.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.dtnm.monitor.model.CheckStatusFactory;
import ru.dtnm.monitor.model.config.alert.AlertAction;
import ru.dtnm.monitor.model.config.alert.AlertConfig;
import ru.dtnm.monitor.model.config.alert.AlertPerson;
import ru.dtnm.monitor.model.config.component.ComponentConfig;
import ru.dtnm.monitor.model.query.MonitoringResult;
import ru.dtnm.monitor.model.status.CheckStatusResponse;
import ru.dtnm.monitor.notification.AlertHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Работа с результатами мониторинга
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
@Component
public class HistoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(HistoryHandler.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AlertHandler alertHandler;

    @Value("${monitor.history.location}")
    private String historyLocation;

    /**
     * Записывает результат последнего опроса в файл
     *
     * @param queryResult результат
     * @param componentConfig информация о компоненте
     * @param alertConfig информация об уведомлениях
     */
    public void handleQuery(
            final MonitoringResult queryResult,
            final ComponentConfig componentConfig,
            final AlertConfig alertConfig,
            final Map<String, String> templates,
            final Map<String, AlertPerson> persons) {
        LOG.debug(">> handleQuery for mnemo={} and componentResponse={}", queryResult.getMnemo(), queryResult);
        final CheckStatusResponse lastCheckResult = getLastCheckResult(queryResult.getMnemo());
        // Если не заполнено в чекере - значит, неудачный опрос и надо поднимать предыдущие результаты
        if (queryResult.getLastOnline() == null) {
            queryResult.setLastOnline(lastCheckResult.getLastResponse() == null
                    ? null
                    : lastCheckResult.getLastResponse().getLastOnline());
        }
        try {
            final CheckStatusResponse toStore = CheckStatusFactory.status(queryResult, componentConfig);
            // Запись данных
            writeHistory(toStore);
            // Уведомление пользователей
            if (!toStore.getStatus().equals(lastCheckResult.getStatus()) && alertConfig != null) {
                final List<AlertAction> actions = alertConfig
                        .getActions()
                        .stream()
                        .filter(e -> e.getStatus().equals(toStore.getStatus()))
                        .collect(Collectors.toList());
                alertHandler.notify(queryResult.getMnemo(), actions, templates, persons, toStore.getReason());
            }
        } catch (Exception e) {
            LOG.error("Unable to handle query result! {}", e.getMessage(), e);
        }
    }

    /**
     * Запись в файл
     *
     * @param stored записываемая сущность
     * @throws IOException
     */
    private void writeHistory(final CheckStatusResponse stored) {
        try {
            final File historyDir = new File(historyLocation);
            if (!historyDir.exists()) historyDir.mkdir();

            final File file = new File(getPath(historyLocation, stored.getLastResponse().getMnemo()));
            if (!file.exists()){
                file.createNewFile();
            }
            final FileOutputStream filesOS = new FileOutputStream(file);
            final String json = objectMapper.writeValueAsString(stored);
            filesOS.write(json.getBytes(StandardCharsets.UTF_8));
            filesOS.close();
        } catch (Exception e) {
            LOG.error("Unable to write history! {}", e.getMessage(), e);
        }
    }


    /**
     * Читает из файла последнюю строчку - и возвращает результат последнего опроса
     *
     * @param mnemo мнемо опрашиваемого компонента
     */
    public CheckStatusResponse getLastCheckResult(final String mnemo) {
        LOG.debug(">> getLastCheckResult for mnemo={}", mnemo);
        CheckStatusResponse result = new CheckStatusResponse();
        final File file = new File(getPath(historyLocation, mnemo));
        try {
            final FileInputStream fis = new FileInputStream(file);
            result = objectMapper.readValue(IOUtils.readStringFromStream(fis), CheckStatusResponse.class);
        } catch (Exception e) {
            LOG.error("Unable to read history: {}", e.getMessage());
        }
        return result;
    }


    private String getPath (final String historyLocation, final String mnemo) {
        return String.format("%s/%s.json", historyLocation, mnemo);
    }


    public String getHistoryLocation() {
        return historyLocation;
    }

    public void setHistoryLocation(String historyLocation) {
        this.historyLocation = historyLocation;
    }
}
