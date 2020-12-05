# Postgres tables load analyzer

## Requirements
- Java 11
- Maven
- PostgreSQL

## Properties
```properties
# порт сервиса
server.port=2789

# параметры базы данных PostgreSQL, в которую будут записываться данные о нагрузке
DB_HOST=localhost
DB_PORT=5432
DB_NAME=analyzer
# пользователь должен иметь права для чтения и записи
DB_USER=postgres
DB_PASSWORD=postgres
DB_SCHEMA=public

# параметры базы данных PostgreSQL, которую нужно анализировать
ANALYZER_DB_HOST=localhost
ANALYZER_DB_PORT=5432
ANALYZER_DB_NAME=analyzer
ANALYZER_DB_USER=postgres
ANALYZER_DB_PASSWORD=postgres

# cron-expression, с какой частотой будет запускаться обновление статистики
MAIN_STAT_CRON_JOB=0/20 * * * * ?
```

## Guide
Для сборки проекта нужно выполнить в консоле команду `mvn clean install`<br>
Далее надо поменять в файле `application.yml` нужные настройки и запустить сервис командой:<br>
`java -jar target/table-analyzer-${version}.jar`, где `version` - актуальная версия сервиса, берется из значения поля `project.version` файла `pom.xml`<br>

## Grafana
Для добавления графиков в Grafana нужно подключить инстанс PostgreSQL к Grafana и импортировать список Dashboard через импорт json файла `grafana-table-analyzer.json`