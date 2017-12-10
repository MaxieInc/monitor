package ru.dtnm.monitor.model.status;

/**
 * Мнемоники проверок - причины присвоения вычисленных статусов
 */
public interface CheckMnemoConstant {

    // 1. Проверка на соответствие HTTP - статуса ответа и конфига
    String HTTP_STATUS_CHECK = "Проверка по Http-статусу ответа";

    // 2. Проверка на длительность ответа
    String CALL_DURATION_CHECK = "Проверка по длительности вызова";

    // 3. Проверка по KeepAlive
    String KEEP_ALIVE_CHECK = "Проверка по KeepAlive";

    // 4. Проверка по числовым метрикам
    String METRICS_CHECK = "Проверка по метрикам";

    // 5. По умолчанию
    String UNKNOWN = "Неизвестно";
}
