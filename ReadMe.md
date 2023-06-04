

## A Coffee Store Service Application


### This project implemented with Java 8, Spring Boot 2.5.1, hibernate and maven

#### This project has 3 profiles (dev, test, prod), and the default profile is dev.
 In order to start the project, first a local PostgreSQL DB is needed. Run the following in order to launch a containerized local PostgreSQL DB
     
     docker-compose up -d db
    
#### After that, by starting the project, the Hibernate Auto DDL is responsible for create the tables and DB migrations.    
    
## Services
The required functionality for the Admin services as well as place order by clients has been provided. The swagger-ui
for APIs and the detail of each service is provided as bellow:
 
### Swagger ui
   
   **http://127.0.1.1:8084/swagger-ui.html**

Note that the Postman collection of APIs is also available in this file: Coffee Order Service.postman_collection.json

#### Admin API
These APIs consumed by Admin of the application:

**Important Note**: As required in the assignment, there is no need for login approach or authentication,
So the only security provided in this project for securing Admin APIs, is considering a secret token, which must be sent
in header with specific header name as SECRET_KEY, and it checked using a custom filter.

Obviously in the real application, we must consider authentication 
and authorization methods using Spring Security by OAuth2 / JWT or probably dynamic role management.

    1. Create new Product (Drink/Topping)
      URL : http://127.0.1.1:8084/coffeeStoreService/v1/admin/product/create
      Method : POST
    
    2. Update an existing product (Drink/Topping)
       URL : http://127.0.1.1:8084/coffeeStoreService/v1/admin/product/update
       Method : PUT
    
    3. Fetch All Drink
      URL : http://127.0.1.1:8084/coffeeStoreService/v1/admin/product/drink/all
      Method : GET
    
    4. Fetch All Topping
        URL :  http://127.0.1.1:8084/coffeeStoreService/v1/admin/product/topping/all
        Method : GET
    
    5. Delete an existing product (Drink/Topping)
        URL : http://127.0.1.1:8084/coffeeStoreService/v1/admin/product/delete
        Method : GET

    6. Find Most Used Products (Drink/Topping)
        URL : http://127.0.1.1:8084/coffeeStoreService/v1/admin/product/mostUsed
        Method : GET
        
#### Coffee Order API
These APIs consumed by clients to order coffee (Drinks and Toppings).

    1. Place a new order 
        URL : http://127.0.1.1:8084/coffeeStoreService/v1/order/create
        Method : POST
        
   **Note**: It is assumed that the client can place an order with multiple drinks, while each drink can be
    combined by multiple toppings. (order drinks with any of the topping combinations)
        
### Run Tests (on dev profile by default)
    mvn test 
    
Note that, the tests can be run and test and prod environments by providing the target environment, 
for example on corresponding CI/CD pipeline.

#### Test Coverage:
The following services are provided with Integration Test using @SpringBootTest (inside the application context).
and unit tests for inner functions such as discount calculator.
Note that we can also add some Persistence Layer tests (using @DataJpaTest).

    - Authorization of Admin
    - Admin Create/Update/Delete product services
    - Admin Get products services
    - Admin Get most used products services
    - Place(Create) Order service by users 
    - Logic of discount calculator in different scenarios
    
So the test coverage of the project should be enough.

# Commands:
Note that we can provide a make file in order to wrap the original commands for each purpose with a alias command, 
but here we provided the original basic commands in order to build, test, run and build the docker image of the project.

### Build the project (It should be environment agnostic)
    mvn clean package
    
Note that by default the tests provided will be run in the build process.    

### Run the project as container
First, we need to get build from the project as a jar file, by:
    
    mvn clean package

Then, by having our database up (as mentioned in the previous sections), in the docker-compose file,
 there is a serivce for run the application. So we start the service by:
    
    docker-compose up -d --build app
    
### Build the docker image
After get build of the project, by passing the target image name and tag, it should be created. For example for the dev environment:

    docker build -t coffe-store-service:dev .

So this image can be built by any name and tag (based on environment), and can be deployed with and container system
such as docker-compose, docker-swarm, k8s (on-premise or on cloud environment).

**Important Note** : As for database, which considered as a PostgreSQL DB, it assumed that in staging/test and prod
 environments, there is a deployed database and we should put its host and credentials on corresponding yml file.
 
 And for the **dev** profile, a containerized PostgreSQL instance is provided by docker-compose (as described above). And
 it should be started (on localhost) before running the application on local machine.
 (by docker-compose up -d db).
 
 Also the credentials of the database can be provided by environment variables at the run time of the application
  (for example by k8s secret object).