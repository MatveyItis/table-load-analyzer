# Postgres tables load analyzer

## Instruction
Для сборки проекта нужно выполнить в консоле команду `mvn clean install`<br>
Далее надо поменять в файле `application.yml` нужные настройки и запустить сервис командой:<br>
`java -jar table-analyzer-${version}.jar`, где `version` - актуальная версия сервиса<br>

## Grafana
Для добавления графиков в Grafana нужно подключить инстанс PostgreSQL к Grafana и импортировать список Dashboard через импорт json файла `grafana-table-analyzer.json`
