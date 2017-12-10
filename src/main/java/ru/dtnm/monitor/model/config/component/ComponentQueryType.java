package ru.dtnm.monitor.model.config.component;

public enum ComponentQueryType {

    QUERY("Опрашивается приложением"),
    POST("Ожидает данных извне");

    private String description;

    ComponentQueryType(final String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
