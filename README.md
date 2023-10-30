# Webpage Analyzer

[![Actions Status](https://github.com/biscof/java-project-72/workflows/hexlet-check/badge.svg)](https://github.com/biscof/java-project-72/actions)
[![build-and-test](https://github.com/biscof/java-project-72/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/biscof/java-project-72/actions/workflows/build-and-test.yml)
[![Maintainability](https://api.codeclimate.com/v1/badges/9badc0da4953c5c15c26/maintainability)](https://codeclimate.com/github/biscof/java-project-72/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/9badc0da4953c5c15c26/test_coverage)](https://codeclimate.com/github/biscof/java-project-72/test_coverage)


## Overview

Webpage Analyzer is a web application built using the Javalin web framework. It offers a simple interface for analyzing webpages for their basic SEO suitability. This app sends a request to user-provided websites, parses the response, and extracts some information, including the response code, page title, `h1` header content, and meta description.

You can access the demo [here](https://web-page-analyzer.onrender.com).


## Technologies and Dependencies

- Java 17
- Javalin 5.5
- eBean 13.15
- Thymeleaf 3.1
- Bootstrap 5.1
- PostgreSQL 42.5
- H2 2.1
- JaCoCo 8.11
- JUnit 5.8


## Usage

### Prerequisites

- JDK 17
- Docker

### Running

1. Clone this repository to your machine and navigate to the project `app` directory:

```bash
git clone https://github.com/biscof/webpage-analyzer.git
cd app
```

2. Use this command to build a docker image:

```bash
make build-docker-image
```

3. Then run the docker container with an H2 in-memory database (suitable for development and experiments):

```bash
make run-docker-image-dev
```
The app will be available on port `8000` of your `localhost`.

4. To run the app in the production environment use this docker command:

```bash
docker run run -d -p 8000:8000 -e APP_ENV=production -e JDBC_DATABASE_USERNAME=<your DB username> -e JDBC_DATABASE_PASSWORD=<your DB password> -e JDBC_DATABASE_URL=<your DB URL> webpage-analyzer
```

Ensure you provide the valid values for `JDBC_DATABASE_URL`, `JDBC_DATABASE_USERNAME`, and `JDBC_DATABASE_PASSWORD` environment variables.


## Testing

Run the tests:

```bash
make test
```
