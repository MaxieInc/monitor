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
import ru.dtnm.monitor.model.config.component.ComponentInfo;
import ru.dtnm.monitor.model.query.ComponentResponse;
import ru.dtnm.monitor.model.status.CheckStatusResponse;
import ru.dtnm.monitor.notification.AlertHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
     * @param currentResponse результат
     * @param componentInfo информация о компоненте
     * @param alertConfig информация об уведомлениях
     */
    public void handleQuery(final ComponentResponse currentResponse, final ComponentInfo componentInfo, final AlertConfig alertConfig) {
        LOG.debug(">> handleQuery for mnemo={} and componentResponse={}", currentResponse.getMnemo(), currentResponse);
        // Если не заполнено в чекере - значит, неудачный опрос и надо поднимать предыдущие результаты
        if (currentResponse.getLastOnline() == null) {
            final ComponentResponse lastResponse = getLastCheckResult(currentResponse.getMnemo()).getLastResponse();
            currentResponse.setLastOnline(lastResponse.getLastOnline());
        }
        try {
            final CheckStatusResponse stored = CheckStatusFactory.status(currentResponse, componentInfo);
            // Запись данных
            writeHistory(stored);
            // Уведомление пользователей
            final List<AlertAction> actions = alertConfig
                    .getActions()
                    .stream()
                    .filter(e -> e.getStatus().equals(stored.getStatus()))
                    .collect(Collectors.toList());
            alertHandler.notify(currentResponse.getMnemo(), actions);
        } catch (IOException ioe) {
            LOG.error("Unable to handle query result! {}", ioe.getMessage(), ioe);
        }
    }

    /**
     * Запись в файл
     *
     * @param stored записываемая сущность
     * @throws IOException
     */
    private void writeHistory(final CheckStatusResponse stored) throws IOException {
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
        CheckStatusResponse result = null;
        final File file = new File(getPath(historyLocation, mnemo));
        try {
            final FileInputStream fis = new FileInputStream(file);
            result = objectMapper.readValue(IOUtils.readStringFromStream(fis), CheckStatusResponse.class);
        } catch (Exception e) {
            LOG.error("Unable to read history: ", e.getMessage(), e);
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
