# Дипломный проект профессии «Тестировщик»
Дипломный проект представляет собой автоматизацию тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.

## Запуск приложения
Для запуска необходимы следующие инструменты:
* Git
* Java Jre 1.8
* браузер Google Chrome
* Docker (Docker Toolbox), либо Node.js 
* СУБД: Mysql 8 или Postgres 12.

1. Запустить Docker Toolbox 
2. Загрузить контейнеры mysql, postgres и образ платежного шлюза nodejs в терминале IDEA командой 
          
    ````
    docker-compose up
    ````
 
3. Во втором терминале запустить SUT командой

   - для конфигурации с базой данный MySql: 
  
    ````
    java -Dspring.datasource.url=jdbc:mysql://192.168.99.100:3306/app -jar artifacts/aqa-shop.jar
    ````
            
   - для конфигурации с базой данных PostgreSQL:
  
    ````
    java -Dspring.datasource.url=jdbc:postgresql://192.168.99.100:5432/app -jar artifacts/aqa-shop.jar
    ```` 
            
4. В браузере открыть SUT в окне с адресом 

    ````
    http://localhost:8080/
    ````
     
5. Запустить автотесты командой 

   -  для конфигурации с MySql
 
    ````
    gradlew test -Dtest.db.url=jdbc:mysql://192.168.99.100:3306/app
    ````
            
   - для конфигурации с postgresql
 
    ````
    gradlew test -Dtest.db.url=jdbc:postgresql://192.168.99.100:5432/app
    ````
6. Остановить SUT командой CTRL + C

7. Остановить контейнеры командой CTRL + C и после удалить контейнеры командой

    ````
    docker-compose down
    ````     
