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
- Java 8 (JDK 1.8) or higher
- Apache Maven 3.x
- Google App Engine SDK

## Setup Instructions

1. Ensure you have **Java 8 (JDK 1.8)** installed. Verify with:

       $ java -version

2. Update the value of `application` in `appengine-web.xml` to the app
   ID you have registered in the App Engine admin console and would
   like to use to host your instance of this sample.

3. Add your API method to `src/main/java/${packageInPathFormat}/YourFirstAPI.java`.

4. Optional step: These sub steps are not required but you need this
   if you want to have auth protected methods.

    1. Update the values in `src/main/java/${packageInPathFormat}/Constants.java`
       to reflect the respective client IDs you have registered in the
       [APIs Console][6]. 

    2. You also need to supply the web client ID you have registered
       in the [APIs Console][4] to your client of choice (web, Android,
       iOS).

## Building and Running

1. Build the project:

       $ mvn clean install

2. Run the application with `mvn appengine:devserver`, and ensure it's
   running by visiting your local server's api explorer's address (by
   default [localhost:8080/_ah/api/explorer][5].)

3. Run the test suite:

       $ mvn test

4. Get the client library with:

       $ mvn appengine:endpoints_get_client_lib

   It will generate a client library jar file under the
   `target/endpoints-client-libs/<api-name>/target` directory of your
   project, as well as install the artifact into your local maven
   repository.

5. Deploy your application to Google App Engine with:

       $ mvn appengine:update

## Java 8 Features Used
This project uses the following Java 8 features:
- **Lambda expressions** for concise anonymous function implementations (e.g., Comparator instances)
- **Method references** for cleaner callback-style code (e.g., `this::transformTo`, `RecommendationEnums::message`)
- **Streams API** for declarative data processing (e.g., filtering, mapping, collecting)
- **Diamond operator** for cleaner generic type instantiation
- **`Map.computeIfAbsent`** for simplified map population patterns

[1]: https://developers.google.com/appengine
[2]: http://java.com/en/
[3]: https://developers.google.com/appengine/docs/java/endpoints/
[4]: https://developers.google.com/appengine/docs/java/tools/maven
[5]: https://localhost:8080/_ah/api/explorer
