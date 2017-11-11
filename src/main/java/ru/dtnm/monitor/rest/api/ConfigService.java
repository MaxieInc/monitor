package ru.dtnm.monitor.rest.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dtnm.monitor.CheckerContainer;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Map;

/**
 * РЕСТ-сервис для управления текущей конфигурацией
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
@RestController
@RequestMapping("/config")
public class ConfigService {

    private Logger LOG = LoggerFactory.getLogger(ConfigService.class);

    @Autowired
    private CheckerContainer checkerContainer;

    /**
     * Возвращает теущую конфигурацию
     * @throws IOException
     */
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON)
    public Map<String, Object> getCurrentConfig() throws IOException {
        LOG.debug(">> getCurrentConfig");
        return checkerContainer.readConfigs();
    }

    /**
     * обновляет конфигурацию в приложении
     */
    @GetMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON)
    public void refreshConfig() {
        LOG.debug(">> refreshConfig");
        checkerContainer.reloadConfig();
    }
}
