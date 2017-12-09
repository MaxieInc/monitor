/**
 * Основной скрипт отрисовки состояния компонентов
 */

/** Переменная для таймера */
var timer = null;
var SPINNER_SHOW_TIMEOUT = 300;   // 300мс виден спиннер
var spinnerTimer = null;

/** Отрисовывает страницу */
function drawPage() {
    $('#monitor_location').append('Приложение мониторинга: ' + MONITOR_APPLICATION_ADDRESS);
    $('#refresh_interval').attr('value', MONITOR_REFRESH_PERIOD / 1000);
    if (REFRESH_ENABLED) {
        startRefresh();
    }
}

/** Старт периодического опроса приложения мониторинга */
function startRefresh() {
    $('#refresh_btn').attr('value', 'Не опрашивать');
    $('#refresh_btn').attr('onclick', 'stopRefresh()');
    $('#refresh_enabled').text('Обновление ВКЛЮЧЕНО');
    startTimer();
}

/** Останов периодического опроса приложения мониторинга */
function stopRefresh() {
    $('#refresh_btn').attr('value', 'Опрашивать');
    $('#refresh_btn').attr('onclick', 'startRefresh()');
    $('#refresh_enabled').text('Обновление выключено');
    stopTimer();
}

/**
 * Стартует периодическое получение данных от приложения монитора
 */
function startTimer() {
    refresh();
    $('#refresh_interval').attr('disabled', true);
    var refreshTimeout = $('#refresh_interval').attr('value') * 1000;
    console.log('Refresh is every ' + refreshTimeout + ' ms');
    timer = setInterval(function() {refresh();}, refreshTimeout);
    console.log('timer enabled');
}

/**
 * Останавливает таймер опроса приложения монитора
 * Если нужно - скрывает спиннер
 */
function stopTimer() {
    clearInterval(timer);
    $('#refresh_interval').attr('disabled', false);
}

function refresh() {
    $('#refresh_indicator').removeAttr('hidden');
    spinnerTimer = setTimeout(function() {
        $('#refresh_indicator').attr('hidden', true);
    }, SPINNER_SHOW_TIMEOUT);
    $('#monitoring_container').empty();
    jQuery.get(MONITOR_APPLICATION_ADDRESS + MONITOR_INFO_URL, null, redrawMonitorPage);
}

/**
 * Перерисовывает страницу по результатам опроса приложения монитора
 *
 * @param data данные (JSON-объект) в ответе от сервиса монитора
 */
function redrawMonitorPage(data) {
    for (var i = 0; i < data.length; i++) {
        var component = data[i];
        var lastResponse = component.lastResponse;
        var element = $('<div></div>');

        var elementId = lastResponse.mnemo;
        $(element).attr('id', elementId);
        $(element).attr('class', 'column ' + component.status);
        $(element).attr('title', component.status);

        $('<p>Мнемо компонента: ' + lastResponse.mnemo + '</p>').appendTo(element);
        $('<p title="' + lastResponse.url + '">Адрес: ' + lastResponse.url + '</p>').appendTo(element);
        $('<p>HTTP - статус: ' + lastResponse.httpStatus + '</p>').appendTo(element);
        $('<p>Длительность ответа: ' + lastResponse.responseDuration + 'мс</p>').appendTo(element);
        var date = new Date(lastResponse.lastOnline);
        $('<p>Последний ответ: ' + date.toString('yyyy-MM-dd HH:mm:ss') + '</p>').appendTo(element);

        element.appendTo('#monitoring_container');
    }
}