package ru.dtnm.monitor.model.status;

/**
 * Перечисление: статус проверки состояния компонента
 *
 * @author M.Belolipov
 * @version 0.1
 * @since 2017-11-05
 */
public enum CheckStatus {

    NOT_CHECKED ("Не проверяется",     -1),
    HEALTHY     ("Работает",            0),
    WARNING     ("Внимание",            1),
    CRITICAL    ("Критический сбой",    2),
    FAILED      ("Не работает",         3),
    UNKNOWN     ("Неизвестен",          4);


    CheckStatus(String description, int number) {
        this.description = description;
    }

    private String description;

    /** Номер для сравнения */
    private int number;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
