Tech Gallery
==================

A skeleton application for Google Cloud Endpoints in Java.

## Products
- [App Engine][1]

## Language
- [Java 8][2]

## APIs
- [Google Cloud Endpoints][3]
- [Google App Engine Maven plugin][4]

## Prerequisites

- **Java 8 (JDK 1.8)** or higher - [Download](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html)
- **Apache Maven 3.x** - [Download](https://maven.apache.org/download.cgi)
- **Google Cloud SDK** (for App Engine deployment) - [Download](https://cloud.google.com/sdk/docs/install)

Verify your Java version:
```bash
java -version   # Should show 1.8.x
javac -version  # Should show 1.8.x
```

## Setup Instructions

1. Update the value of `application` in `appengine-web.xml` to the app
   ID you have registered in the App Engine admin console and would
   like to use to host your instance of this sample.

1. Add your API method to `src/main/java/${packageInPathFormat}/YourFirstAPI.java`.

1. Optional step: These sub steps are not required but you need this
   if you want to have auth protected methods.

    1. Update the values in `src/main/java/${packageInPathFormat}/Constants.java`
       to reflect the respective client IDs you have registered in the
       [APIs Console][6]. 

    1. You also need to supply the web client ID you have registered
       in the [APIs Console][4] to your client of choice (web, Android,
       iOS).

## Building and Running

1. Compile the project:

   ```bash
   mvn clean compile
   ```

1. Run the tests:

   ```bash
   mvn test
   ```

1. Run the application with `mvn appengine:devserver`, and ensure it's
   running by visiting your local server's api explorer's address (by
   default [localhost:8080/_ah/api/explorer][5].)

1. Get the client library with

   ```bash
   mvn appengine:endpoints_get_client_lib
   ```

   It will generate a client library jar file under the
   `target/endpoints-client-libs/<api-name>/target` directory of your
   project, as well as install the artifact into your local maven
   repository.

1. Deploy your application to Google App Engine with

   ```bash
   mvn appengine:update
   ```

## Java 8 Upgrade Notes

This project was upgraded from Java 7 to Java 8. The following changes were made:

- **Build configuration**: Updated `maven-compiler-plugin` in `pom.xml` to use source/target `1.8`
- **Streams API**: Replaced traditional for-loops with Java 8 streams for collection processing in service implementations
- **Lambda expressions**: Replaced anonymous inner classes with lambdas (e.g., `Comparator` implementations, `Work` interface)
- **Method references**: Used method references for cleaner code (e.g., `TechnologyOrderOptionEnum::option`)
- **Diamond operator**: Simplified generic type declarations using `<>` inference
- **java.time API**: Replaced `java.util.Calendar` usage with `java.time.Instant` and `java.time.LocalDate`
- **java.util.Base64**: Replaced `javax.xml.bind.DatatypeConverter` with `java.util.Base64`
- **Map.computeIfAbsent**: Simplified map population patterns using `computeIfAbsent`

[1]: https://developers.google.com/appengine
[2]: http://java.com/en/
[3]: https://developers.google.com/appengine/docs/java/endpoints/
[4]: https://developers.google.com/appengine/docs/java/tools/maven
[5]: https://localhost:8080/_ah/api/explorer
