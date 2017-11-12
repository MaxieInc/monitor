package ru.dtnm.monitor.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.helpers.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.dtnm.monitor.model.QueryResult;

import java.io.*;
import java.nio.charset.StandardCharsets;

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

    @Value("${monitor.history.location}")
    private String historyLocation;

    /**
     * Записывает результат последнего опроса в файл
     *
     * @param queryResult результат
     */
    public void writeHistory(final QueryResult queryResult) {
        LOG.debug(">> writeHistory for mnemo={} and queryResult={}", queryResult.getMnemo(), queryResult);
        try {
            final File historyDir = new File(historyLocation);
            if (!historyDir.exists()) historyDir.mkdir();

            final File file = new File(getPath(historyLocation, queryResult.getMnemo()));
            if (!file.exists()){
                file.createNewFile();
            }
            final FileOutputStream filesOS = new FileOutputStream(file);
            final String json = objectMapper.writeValueAsString(queryResult);
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
    public QueryResult getLastCheckResult(final String mnemo) {
        LOG.debug(">> getLastCheckResult for mnemo={}", mnemo);
        QueryResult result = null;
        final File file = new File(getPath(historyLocation, mnemo));
        try {
            final FileInputStream fis = new FileInputStream(file);
            result = objectMapper.readValue(IOUtils.readStringFromStream(fis), QueryResult.class);
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
