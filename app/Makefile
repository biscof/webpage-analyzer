#Makefile

prepare:
	./gradlew clean installDist

build-and-check:
	./gradlew clean build test checkstyleMain checkstyleTest

report:
	./gradlew jacocoTestReport

build-docker-image:
	docker build --tag 'webpage-analyzer' .

run-docker-image-dev:
	docker run --detach -p 8000:8000  webpage-analyzer

.PHONY: build
