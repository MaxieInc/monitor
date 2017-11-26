package ru.dtnm.monitor.model.config.component;

import java.util.List;

/**
 * Конфиг объектов мониторинга
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class MonitorConfig {

    private List<ComponentConfig> components;


    public List<ComponentConfig> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentConfig> components) {
        this.components = components;
    }
}
