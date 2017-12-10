# monitor
Приложение для мониторинга удалённых компонентов, отвечающих по http.

1. Сборка проекта **mvn clean install**
2. Запуск - положить в один каталог джарник, конфиг и JS - конфиг, в конфигах всё записать как надо, и java -jar джарник.
3. Просто опрашивает УРЛы и записывает в файлы их состояния. всё.

Польовательский интерфейс приделаю немного позже.

Краткое описание РЕСТ-методов

1. **ГЕТ** на УРЛ http://localhost:8888/monitor/config/ - получение данных о текущией конфигруации
2. **ГЕТ** на УРЛ http://localhost:8888/monitor/config/refresh - перечитывает текущую конфигурацию из файлов
3. **ГЕТ** на УРЛ http://localhost:8888/monitor/info/all - все результаты мониторинга, по всем опрашиваемым колмпонентам
4. **ГЕТ** на УРЛ http://localhost:8888/monitor/info/{component_mnemo} - результат опроса указанного компонента
5. **ПОСТ** на УРЛ http://localhost:8888/monitor/accept/{component_mnemo} - отправить в монитор сообщение о состоянии компонента. Принимается JSON в UTF-8 вида:


_{
  "metrics": [
    {
      "mnemo": "component.call.duration",
      "value": 12.5
    },
    {
      "mnemo": "some.metric.mnemo.n",
      "value": 14
    }
  ],
  "properties": [
    {
      "mnemo": "url",
      "value": "http://yandex.ru"
    }
  ]
}`_

Такой же ответ ожидается от конкретного компонента при опросе его монитором.


Внимание!!!
 Для рассылки уведомлений мониторинга зарегана новая почта
 ite-monitor@mail.ru
 Qq12345678
 
 
 
Рисовалка для диаграмм - онлайн, там можно прям с диска файл использовать.
https://www.draw.io/
Сам файл диаграммы лежит в ./doc/Architecture.xml 