package ru.dtnm.monitor.model.config;

/**
 * Описание единственного объекта наблюдения
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public class ConfigComponent {

    private String mnemo;
    private String url;

    public String getMnemo() {
        return mnemo;
    }

    public void setMnemo(String mnemo) {
        this.mnemo = mnemo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
