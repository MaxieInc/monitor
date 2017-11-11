package ru.dtnm.monitor.model.config.alert;

import java.io.Serializable;
import java.util.List;

/**
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class AlertConfigContainer implements Serializable {

    private List<AlertConfig> alertConfigs;


    public List<AlertConfig> getAlertConfigs() {
        return alertConfigs;
    }

    public void setAlertConfigs(List<AlertConfig> alertConfigs) {
        this.alertConfigs = alertConfigs;
    }
}
