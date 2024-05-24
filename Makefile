.DEFAULT_GOAL := build-run

restart:
	mvn clean install
#пересборка проекта + упаковка в jar-файлы

depend:
	mvn dependency:tree
#показать все зависимости

spring:
	   mvn spring-boot:run
#запустить спринг-бут приложение

test:
	mvn test

.PHONY: build