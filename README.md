# Intermine Authenticaion Server

<img src="https://github.com/ry007/intermine-authentication-server/blob/dev/src/main/resources/static/new_logo.png" align="right"
     title="Intermine Logo" width="170" height="150">


This is an Intermine Authorization Server built with Spring Boot 2.x OAuth2 which allows to access all 30 mines instance using a single account.







### Features

* OAuth2.0 Authorization Server
* We Use Postgresql
* We Use Jdbc Token Store
* We Use Jpa

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


NOTE: Resource directory contains two predefined script also for database table creation and dummy entries so no need to create any tables on your own.
But if you are running system very first time then make sure the extension of data file in resource directory is .sql because it will create entries in the tables with primary keys so after first run either remove that file or rename it to data.txt otherwise it'll cause error in your furthur run.  

### STEP-2 Run the application

Server will be up on default port 8282 but you can change it by make changes in application.yml file.   

### Oauth2 Users Credential
| User            | Password        | Role      | Permission                            |
|-----------------|-----------------|-----------|---------------------------------------|
| admin           | admin123        | Admin     | Create,Read,Update & DeleteProfile    |
| testUser        | testUser123     | Operator  | Read & Update Profile                 |

### Oauth Client Credential
| Client_id    | Client_secret  | authorized grant types                             | scope       |
|--------------|----------------|----------------------------------------------------|-------------|
| flymine      | fmine          | authorization_code,password,refresh_token,implicit | READ, WRITE |   



## Testing the App

1. Testing PASSWORD Grant type to get access_token.
This grant type is for testing purpose only and will not present in live application of Intermine authorization server.

Make a post request from your rest client or by curl with following parameters:
```
url: http://localhost:8282/oauth/token
Authorization: Basic Auth
               username(client_id): flymine
               password(client_secret): fmine
Body(form-data):: grant_type : password
                  username   : admin
                  password   : admin123
```


Response:
```
{
    "access_token": "a6d92574-ded3-4a6e-ac7e-6fd8950d8faf",
    "token_type": "bearer",
    "refresh_token": "db8f3bc2-ff39-4480-84a5-d48a80f21311",
    "expires_in": 3509,
    "scope": "READ WRITE"
}
```
In the above grant type client is asking for access token from authorization server with its own credentials on behalf of user i.e along with user credentials also.


2. Testing the authorization_code grant type:

For this grant type we need a client and a resource but this is our auth server only so we can test this grant type by using any rest client like postman.

Choose Authorization as OAuth2.0 in your rest client and request a new access token with the following parameters.

```
Token Name: <your token name>
Grant Type: Authorization Code
Callback Url: http://localhost:8080/code
Auth Url: http://localhost:8282/oauth/authorize
Access Token Url: http://localhost:8282/oauth/token
Client Id: flymine
Client Secret: fmine
Scope: READ WRITE
State:(optional)
Client Authentication: Send as Basic Auth Header
```

Response

you will be redirected to a login screen page so make sure to enter correct credentials of admin or testUser.
After that approve the scope that client want to access.
Finally you will get you token!!!!
```
{
    "access_token": "a6d92574-ded3-4a6e-ac7e-6fd8950d8faf",
    "token_type": "bearer",
    "refresh_token": "db8f3bc2-ff39-4480-84a5-d48a80f21311",
    "expires_in": 3509,
    "scope": "READ WRITE"
}
```

In above grant type our rest client is handling resource server part and our client(website) so whenever we are requesting an access token that means we are trying to access the resources on client thus rest client first redirect us to login page and approval page of auth server and after our approval aur oauth server is giving authorization code to to our rest client and finally it Getting the access_token with the help of that code.



3. Testing validation of access token:
In real application only resource server can validate this token but for here we can test by following request:
Make a Get request with following paramters
```
url: http://localhost:8282/oauth/check_token?token=<your access token>

```
Response :
```

{
    "aud": [
        "inventory",
        "payment"
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
    "client_id": "flymine"
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
