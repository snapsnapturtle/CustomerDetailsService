# CustomerDetailsService

The customer details service is responsible for importing customer data, enriching the data and returning it through an
http api to other consumers.

## Starting the service

The service runs on port 8080 by default and can be started using maven.

```shell
./mvnw spring-boot:run
```

## Importing data

By default, the service reads the `customers.json` file in the `resources` of the project and imports the data after the
application has started and stores the information in an in-memory h2 database.

In addition to the automated import during start up, an endpoint exists, which allows updating the data while the service
is running. It accepts the same format as the `customers.json` file in the `resources` folder. You can find the example
curl command below. All fields are optional, except the `Customer UUID`, as it is used as the identifier in the database.
Any additional fields will be ignored, but can be consumed after updating the corresponding data classes.

```shell
curl -X "POST" "http://localhost:8080/v1/import-customers" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "customers": [
    {
      "Customer Store Number": 20,
      "Customer Number": 100,
      "Customer Commercial Name": "Tratoria Luigi",
      "Customer Name": "Pizzeria Luigi Gmbh",
      "Customer UUID": "acf14674-f7b6-44f8-8f4a-c14ca4734e3c",
      "Customer Address": "Berliner Strasse 1, 13189 Berlin, Germany"
    }
  ]
}'
```

## Enriching data

When importing the data, the `GeoCodingService` resolves the address to latitude and longitude. In this project, the 
`GeoCodingController` is a controller, which returns random coordinates, mocking an external service. If no address
exists, the service will not be called.

The feign-client communicates with the mock controller through an http request. To manually request the random
coordinates, the following curl command can be used.

```shell
curl -X "POST" "http://localhost:8080/geocode" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "address": "Berliner Strasse 1, 13189 Berlin, Germany"
}'
```

## Retrieving the data

After the data has been imported through the start-up or the endpoint, the enriched data can be retrieved with two
endpoints.

### Single customer

Fetching information about a single customer can be done using the following curl command, providing the customer uuid
in the path.

```shell
curl "http://localhost:8080/v1/customers/acf14674-f7b6-44f8-8f4a-c14ca4734e3c"
```

### All customers

In addition to fetching a single customer, all customers can be requested from the service. To prevent the service from
returning all customers at once, the response is paged. The default page size is 10 items, which can be overridden in 
the request with the parameters, based on the clients needs. See the curl command below to request the imported
customers.

```shell
curl "http://localhost:8080/v1/customers?size=15&page=0"
```

It is also possible to sort the results from this endpoint. To sort the list, the `sort` parameter with the column name
and sort direction must be included in the request. In the example, the results are sorted ascending by their commercial
name. The `sort` parameter can be combined with the pagination parameters mentioned above.

```shell
curl "http://localhost:8080/v1/customers?sort=commercialName,asc"
```
