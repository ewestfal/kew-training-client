# kew-training-client

A Kuali Enterprise Workflow "embedded" client application used for training purposes.

This is a Spring Boot application, so is configured to run as such.

NOTE: This client currently depends on 2.6.0-M1-SNAPSHOT of Kuali Rice since that version has been
updated to a newer version of Spring that is compatible with Spring Boot. You will likely need to
clone this version from https://github.com/kuali/rice (currently on master) and execute a `mvn clean install` in order to install it into your local Maven repository.

## Dependencies

You must have a fully functional Kuali Rice standalone server (2.5.x or later) running for this client
application to communicate with. You must ensure that any certificates between the client application
and standalone server have been imported and trusted in each keystore.

## Configuration

To run this application, it will need a configuration file passed to it in the following form:

```
server.port: 8081
application.url: http://localhost:8081
keystore:
  file: /usr/local/rice/rice.keystore
  alias: rice
  password: xxxxxx
rice:
  server.url: http://localhost:8080/rice-server
  ojb.platform: MySQL
  platform: org.kuali.rice.core.framework.persistence.platform.MySQLDatabasePlatform
  datasource:
    driverProperties.user: xxxxxx
    driverProperties.password: xxxxxx
    driverProperties.url: jdbc:mysql://localhost:3306/xxxxxx
    driverProperties.driverClassName: com.mysql.jdbc.Driver
    testQuery: select 1
    minPoolSize: 10
    maxPoolSize: 10
    platform: org.kuali.rice.core.framework.persistence.platform.MySQLDatabasePlatform
datasource:
  driverProperties.user: yyyyyy
  driverProperties.password: yyyyyy
  driverProperties.url: jdbc:mysql://localhost:3306/yyyyyy
  driverProperties.driverClassName: com.mysql.jdbc.Driver
  testQuery: select 1
  minPoolSize: 10
  maxPoolSize: 10
nonTxDatasource:
  username: yyyyyy
  password: yyyyyy
  url: jdbc:mysql://localhost:3306/yyyyyy
  driverClassName: com.mysql.jdbc.Driver
```

The above example is in YAML format, but could be .properties file format if you prefer. Customize the values as necessary for your environment.

You will need to pass that file to your application when started:

```java -jar -Dspring.config.location=/path/to/config.yaml target/kew-training-client-0.0.1-SNAPSHOT.war```

## Using Webpack Dev Server

If running in dev or an IDE, you will likely want to run the webpack dev server so that js and css changes get automatically bundled and updated in the browser. To do this, you will need to be sure that you have node and npm installed and available on your path.

Then, when launching the application, use the "dev" profile, as per the following example:

```java -jar -Dspring.profiles.active=dev -Dspring.config.location=/path/to/config.yaml target/kew-training-client-0.0.1-SNAPSHOT.war```

When the application starts up you should see webpack-related output in the console. When you make changes to css or js within `src/main/resources/static`, if you have the application already open in a browser window, it should be refreshed automatically. 