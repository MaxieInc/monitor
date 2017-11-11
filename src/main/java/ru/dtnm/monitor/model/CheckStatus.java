package ru.dtnm.monitor.model;

/**
 * Перечисление: djpvj;yst cnfnecs vjybnjhbyuf rjvgjytynf
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public enum CheckStatus {

    HEALTHY     ("Работает"),
    WARNING     ("Внимание"),
    CRITICAL    ("Критический сбой"),
    FAILED      ("Не работает");


    CheckStatus(String description) {
        this.description = description;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
