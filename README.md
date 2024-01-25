# CustomerDetailsService

The customer details service is responsible for importing customer data, enriching the data and returning it through an api to other consumers. It is using the following technologies:

* Java 21
* Spring Boot
* Database H2 (In-Memory)
* Gradle

## Starting the service

Ensure that Java 21 is available. The service runs on port `8080` by default and can be started using the following command.

```shell
./gradlew bootRun
```

## Example requests

The exposed endpoints of this service can be found in the http files in the `/http` folder. The requests can be executed by using a rest client like the built-in rest client in IntelliJ.

## Importing data

By default, the service reads the `customers.json` file in the `resources` of the project and imports the data after the application has started and stores the information in an in-memory database.

In addition to the automated import during start up, data can be imported using an enpoint, which allows updating the data while the service is running. It accepts the same format as the `customers.json` file in the `resources` folder. All fields are optional, except the `Customer UUID`, as it is used as the identifier in the database. Any new fields in the imported data will be ignored, but can be added to the corresponding data classes for future extension.

## Enriching data

When importing the data, the `GeoCodingService` resolves the address to latitude and longitude. In this project, the `GeoCodingController` is a controller, which returns random coordinates, mocking an external service. If no address exists, the service will not be called. The feign-client communicates with the mock controller in this service. 

## Retrieving data

After the data has been imported, the enriched data can be retrieved with two endpoints.

### Single customer

Fetching information for a single customer can be done using the customer uuid in the path.

### All customers

In addition to fetching a single customer, all customers can be requested from the service; the response is paged. The default page size is 10 items, which can be overridden in the request with the parameters, based on the clients needs. 

It is also possible to sort the results from this endpoint. To sort the list, the `sort` parameter with the column name and sort direction must be included in the request.
