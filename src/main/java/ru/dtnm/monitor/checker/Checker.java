package ru.dtnm.monitor.checker;

/**
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public abstract class Checker {

    // Мнемоника опрашиваемого компонента
    protected String mnemo;
    // Урл опрашиваемого компонента
    protected String url;

    public String getMnemo() {
        return mnemo;
    }

    public String getUrl() {
        return url;
    }
}
