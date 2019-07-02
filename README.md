# Intermine Authenticaion Server

<img src="https://github.com/ry007/intermine-authentication-server/blob/dev/src/main/resources/static/new_logo.png" align="right"
     title="Intermine Logo" width="170" height="150">


This is an Intermine Authorization Server built with Spring Boot 2.x OAuth2 which allows to access all 30 mines instance using a single account.







### Features

* OAuth2.0 Authorization Server
* Postgresql Database
* Jdbc Token Store
* Jpa

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them:

* [Postgresql](https://www.postgresql.org/download/linux/ubuntu/) - Database
* [Postman](https://www.getpostman.com/downloads/) - Rest Client (OPTIONAL)
* IDE- OPTIONAL

## Installing

A step by step series of examples that tell you how to get a development env running.

### STEP-1 Database Configuration

Create your own database in postgresql.

```
CREATE DATABASE <your database name> ;
```
Create a user and grant access to that user.

```
CREATE USER <your user> WITH ENCRYPTED PASSWORD <'yourpass'>;
GRANT ALL PRIVILEGES ON DATABASE <your dbname> TO <your user>;
```

Update these configurations in application.yml file of project inside resource directory.


NOTE: Resource directory contains a predefined script for database table creation so no need to create any tables on your own.  

### STEP-2 Run the application

Clone this repo on your local system and follow these commands:

i. Change directory to this project

```
cd intermine-authorization-server/
```

ii. Build using mvn

```
mvn pacakage
```

iii. Run jar file using java

```
java -jar target/authserver-0.0.1-SNAPSHOT.jar
```

Server will be up on default port 8282 but you can change it by make changes in application.yml file.   


## Testing the App

1. Create a New account on IM auth server

Open http://localhost:8282/intermine/login in any web browser. You will be redirected to a login screen page and thus create a new account.

<p align="center">
  <img src="https://github.com/ry007/intermine-authentication-server/blob/dev/src/main/resources/static/login.png" alt="Intermine Login" width="500">
</p>

2.  Register a new client


Only those user can register a client on Intermine auth server which are having account on it. Open http://localhost:8282/intermine in any web browser and you will be redirected to Intermine auth server dashboard from which you can register a new client after login.

<p align="center">
  <img src="https://github.com/ry007/intermine-authentication-server/blob/dev/src/main/resources/static/client_registration.png" alt="Intermine Login" width="500">
</p>

Response

You will get your client id and secret. Make sure to store these somewhere.

<p align="center">
  <img src="https://github.com/ry007/intermine-authentication-server/blob/dev/src/main/resources/static/client_credentials.png" alt="Intermine Login" width="500">
</p>


3. Testing the authorization_code grant type:

For this grant type we need a client and a resource but this is our auth server only so we can test this grant type by using any rest client like postman.

Choose Authorization as OAuth2.0 in your rest client and request a new access token with the following parameters.

```
Token Name: <your token name>
Grant Type: Authorization Code
Callback Url: http://localhost:8080/code
Auth Url: http://localhost:8282/oauth/authorize
Access Token Url: http://localhost:8282/oauth/token
Client Id: <your-client-id>
Client Secret: <your-client-secret>
Scope: READ WRITE
State:(optional)
Client Authentication: Send as Basic Auth Header
```

Response

You will be redirected to a login screen page so make sure to enter correct credentials of admin.

<p align="center">
  <img src="https://github.com/ry007/intermine-authentication-server/blob/dev/src/main/resources/static/login.png" alt="Intermine Login" width="500">
</p>


After that you will be redirected to approve the scope that client want to access.

<p align="center">
  <img src="https://github.com/ry007/intermine-authentication-server/blob/dev/src/main/resources/static/approval.png" alt="Intermine Login" width="500">
</p>



Finally you will get your token!!!!

```
{
    "access_token": "a6d92574-ded3-4a6e-ac7e-6fd8950d8faf",
    "token_type": "bearer",
    "refresh_token": "db8f3bc2-ff39-4480-84a5-d48a80f21311",
    "expires_in": 3509,
    "scope": "READ WRITE"
}
```


4. Testing validation of access token:
In real application only resource server can validate this token but for here we can test by following request:
Make a Get request with following paramters
```
url: http://localhost:8282/oauth/check_token?token=<your access token>

```
Response :
```

{
    "aud": [
    "Resource-1",
    "Resource-2"
    ],
    "user_name": null,
    "scope": [
        "READ",
        "WRITE"
    ],
    "active": true,
    "exp": 1558779057,
    "authorities": [
        "ROLE_admin",
        "delete_profile",
        "update_profile",
        "read_profile",
        "create_profile"
    ],
    "client_id": "1adh34gdt6yf.apps.intermine.com"
}
```
5. Testing user-info endpoint :

Make a get request on http://localhost:8282/intermine/user-info without any parameters.

If you've already logged in then will get the user's information otherwise you'll be redirected to login page.
Enter correct credentials of user.

Response:

```

{
    "username": "user"
    "email":"user@intermine.com"
}
```

## Running the tests


### Break down into end to end tests

```
example
```

### And coding style tests

```
 example
```

## Deployment

Deploy this on a live system

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - Used to create stand-alone, production-grade Spring based Applications.
* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/url-of-file) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning


## Authors

* **Intermine** - *Initial work* - [ry007](https://github.com/ry007)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the GNU Lesser General Public License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
