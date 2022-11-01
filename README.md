# api-webui-testing
Framework for API and WebUI Testing using Serenity Cucumber Java

## Pre-requisites
 ```
* Java - 1.8
* IDE
* Maven
```

## Installation
 ```
* git clone project : <TBD>
* cd api-webui-testing
* mvn clean install -DskipTests

#RUN API TESTS

* mvn clean verify -Dcucumber.filter.tags="@api-testing"

#RUN UI TESTS
* mvn clean verify -Dcucumber.filter.tags="@ui-testing"

```

## Reports

```
Html Reports at the end of execution is available as `~/target/site/index.html'
Summary Html Reports at the end of execution is available as `~/target/site/serenity-summary.html'
```

## More information

```
* Feature files - ~/src/test/resources/features
* Environment Config file - ~/src/test/resources/serenity.conf
* Webdriver Config file - ~/serenity.properties
* Api StepDefinition - ~/src/test/java/org/example/core/api
* UI StepDefinition - ~/src/test/java/org/example/core/ui/steps


```