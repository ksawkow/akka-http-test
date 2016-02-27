# akka-http-test

## Abstract
Simple Scala example utilizing following technologies:
* Akka HTTP
* Akka Actors
* (Un)marshalling JSON requests/responses
* Mongo reactive-streams driver usage

## Running locally

1. Checkout the source code
2. In project root folder execute the following command:

	`./activator run`

## Additional sbt tasks and reports

* `./activator test` - generates tests report to `target/test-reports/index.html`
* `./activator dependencyUpdatesReport` - generates dependencies report to `target/dependency-updates.txt`
* `./activator dependencyUpdates` - prints report on possible dependencies updates
