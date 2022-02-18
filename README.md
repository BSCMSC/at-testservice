# Автотесты на микросервис testService

При написании автотестов применялся стек - TestNG + Rest-Assured + Allure Report

#### Настройки запуска тестов
Набор тестовых сценариев находится в файле tests.xml. Расположен - src/test/resources/tests.xml  

#### Запуск тестов и сборка отчета

```
mvn clean test allure:report
```

Пояснение к командам:
=========================

```mvn
clean - очистка проекта
```

```mvn
test - запуск тестов
```

```mvn
allure:report - выгрузка allure отчетов в директорию - \target\site\allure-maven-plugin\index.html 
```

Для запуска тестов из под Idea необходимы плагины:
Lombok
TestNG

Настроить конфигурацию на использование Java 8
 1. Run -> Edit Configurations
 2. в окне Run/Debug  Configurations перейти к Templates -> TestNG
 3. в поле JRE выбрать 1.8
 
 