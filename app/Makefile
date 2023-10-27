#Makefile

prepare:
	./gradlew clean installDist

build-and-check:
	./gradlew clean build test checkstyleMain checkstyleTest

report:
	./gradlew jacocoTestReport

docker-db-start:
	docker-compose -f docker/docker-compose.yml up -d -V --remove-orphans

.PHONY: build
