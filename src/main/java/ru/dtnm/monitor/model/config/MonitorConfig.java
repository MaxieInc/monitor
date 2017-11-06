package ru.dtnm.monitor.model.config;

import java.util.List;

/**
 * Конфиг объектов мониторинга
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class MonitorConfig {

    private List<ConfigComponent> components;

    public List<ConfigComponent> getComponents() {
        return components;
    }

    public void setComponents(List<ConfigComponent> components) {
        this.components = components;
    }
}
