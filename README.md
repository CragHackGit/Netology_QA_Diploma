# Курсовой проект по модулю «Автоматизация тестирования»
Курсовой проект представляет собой автоматизацию тестирования сервиса покупки тура в путешествие. Возможна покупка двумя разными способами. После заполнения формы сервис заносит информацию о покупке тура в собственную БД и через API отсылает данные банковским сервисам на обработку.

## Документы проекта
- <a href="https://github.com/CragHackGit/Netology_QA_Diploma/blob/main/docs/Plan.md">План автоматизации</a>
- <a href="https://github.com/CragHackGit/Netology_QA_Diploma/blob/main/docs/Report.md">Отчет о тестировании</a>
- <a href="https://github.com/CragHackGit/Netology_QA_Diploma/blob/main/docs/Summary.md">Итоги</a>

## Порядок развертывания проекта и запуска автотестов
1. Склонировать <a href="https://github.com/CragHackGit/Netology_QA_Diploma">Дипломный проект</a> в локальный репозиторий  и открыть его в IntelliJ IDEA
2. Запустить Docker Desktop
3. В терминале IDEA запустить контейнеры с помощью команды:  
`docker-compose up -d`
4. Запустить тестируемое приложение:  
для mySQL:  
`java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar`  
для postgresgl:  
`java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar`
5. Открыть второй терминал и ввести команду для запуска тестов:  
для mySQL:  
`./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"`  
для postgresgl:  
`./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`
6. Для генерации отчета тестирования используется команда:  
`./gradlew allureServe`
7. Для завершения работы allureServe используется сочетание клавиш:  
`Ctrl+C`
9. Для остановки работы контейнеров используется команда:  
`docker-compose down`
