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

    private List<ComponentInfo> components;


    public List<ComponentInfo> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentInfo> components) {
        this.components = components;
    }
}
