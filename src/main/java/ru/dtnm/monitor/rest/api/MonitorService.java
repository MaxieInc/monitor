package ru.dtnm.monitor.rest.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
@RestController
@RequestMapping("/info")
public class MonitorService {

    @GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON)
    public Map<String, Object> ping() {
        return new HashMap() {{
            put("status", "ok");
        }};
    }
}
