package ru.dtnm.monitor.model.status;

/**
 * Мнемоники проверок - причины присвоения вычисленных статусов
 */
public interface CheckMnemoConstant {

    // 1. Проверка на соответствие HTTP - статуса ответа и конфига
    String HTTP_STATUS_CHECK = "Проверка по Http-статусу ответа";

    // 2. Проверка по KeepAlive
    String KEEP_ALIVE_CHECK = "Проверка по KeepAlive";

    // 3. Проверка по числовым метрикам
    String METRICS_CHECK = "Проверка по метрикам";

    // 4. Проверка на длительность ответа
    String PROPERTIES_CHECK = "Проверка по строковым свойствам";

    // 5. По умолчанию
    String UNKNOWN = "Неизвестно";
}
