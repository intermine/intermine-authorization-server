# InterMine Authorization Server

<img src="https://github.com/ry007/intermine-authentication-server/blob/dev/src/main/resources/static/new_logo.png" align="right"
     title="Intermine Logo" width="170" height="150">


This is an InterMine Authorization Server built with Spring Boot 2.x OAuth2 which allows to access all 30 mines instance using a single account.







### Features

* OAuth2.0 Configured Server
* User login & registration
* Client registration
* User dashboard
* Admin dashboard
* Migration Supported
* Cross domain SSO Supported
* Easy configurations
* More Secure

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them:

* [Postgresql](https://www.postgresql.org/download/linux/ubuntu/) - Database
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

### STEP-3 Admin credential/Account

Below queries will create an Admin account with following credentials:
```
INSERT INTO role(id, name) VALUES (1, 'ROLE_ADMIN');
```
```
INSERT INTO users(user_id, name, username, password, email, enabled, accountnonexpired, credentialsnonexpired, accountnonlocked) VALUES (2, 'Admin','admin','73f841d7321aa6ae28a8d8989d100416', 'admin@intermine.org', true, false, false, false);
```
```
INSERT INTO role_user(row_id, id, user_id) VALUES (2, 1, 2);
```

| Admin Username  | Admin Password  |
|-----------------|-----------------|
|      admin      |    Admin@123    |


## Testing the App

### A. Intermine Authorization Server

1. Create a New account on IM auth server

Open http://localhost:8282/intermine in any web browser. You will be redirected to home page of authorization server from where you can choose profile from the menu bar. Once you open your profile you will be redirected to login screen page and can create a new account on this IM auth server.

<p align="center">
  <img src="https://github.com/rahul-y/intermine-authorization-server/blob/integration/src/main/resources/static/login_screen.png" alt="Intermine Login" width="500">
</p>

2. User Dashboard

Once you logged in successfully then will be redirected to your dashboard from where you can manage all your client and your profile. You can register a new client or can also manage your already registered clients. From dashboard you can also change your account password.

<p align="center">
  <img src="https://github.com/rahul-y/intermine-authorization-server/blob/integration/src/main/resources/static/user_dashboard.png" alt="Intermine Login" width="500">
</p>

3. Register a new client

Only those user can register a client on InterMine auth server which are having account on it. Click on the register client bar from your dashboard and fill up the client registration form with some basic required information

<p align="center">
  <img src="https://github.com/rahul-y/intermine-authorization-server/blob/integration/src/main/resources/static/client_registration.png" alt="Intermine Login" width="250" height="400">
</p>

You will get your client id and secret once InterMine admin verify it.

4. Admin Dashboard

You can login from same login page with the admin account credentials and on successful login you will be redirected to admin dashboard.
Here are some features of admin dashboard:

* Can manage all the user accounts
* Can manage all the registered clients
* Can verify the clients

Verify any registered client with a simple click on verify button

<p align="center">
  <img src="https://github.com/rahul-y/intermine-authorization-server/blob/integration/src/main/resources/static/admin_verify.png" alt="Intermine Login" width="500">
</p>

5. Client Management

User can manage registered client and access their credentials too from dashboard and is also able to update & delete the clients.

<p align="center">
  <img src="https://github.com/rahul-y/intermine-authorization-server/blob/integration/src/main/resources/static/client_management.png" alt="Intermine Login" width="250" height="400">
</p>

### B. Configuring IM Auth Server On client/Mine

1. Client credentials:

Once you get your client credentials i.e client id and secret then add following lines in your mine property file.
For example we can add these in biotestmine.properties file:

```
oauth2.providers = IM
```
```
oauth2.IM.client-id = 6870ca9d7e8545e606e26d51fc5b810b53952eaf.apps.intermine.com
```
```
oauth2.IM.client-secret = 3fb6de6cef8a94c52b4e33e06802e56ce3ebc809
```

That's all!!! Deploy your mine and then anyone can login in your mine with IM auth server.

2. You can choose IM from the dropdown to login with IM account

<p align="center">
  <img src="https://github.com/rahul-y/intermine-authorization-server/blob/integration/src/main/resources/static/login_with_mine.png" alt="Intermine Login" width="500">
</p>

3. Migration

Once you logged in first time on any mine with the IM account, will be redirected to a merge pop up where you will be asked to merge your previous account of mine if have any otherwise can go with No.

<p align="center">
  <img src="https://github.com/rahul-y/intermine-authorization-server/blob/integration/src/main/resources/static/merge_popup.png" alt="Intermine Login" width="500">
</p>


i. If you don't have any previous account you can go with NO. Once you taps on No then you will be asked to give access of your name and email to the client/mine.
If you allow it then you will be successfully logged in to the mine otherwise deny will not logged you in to mine

<p align="center">
  <img src="https://github.com/rahul-y/intermine-authorization-server/blob/integration/src/main/resources/static/authorize_popup.png" alt="Intermine Login" width="500">
</p>


ii. If you want to merge your pervious mine account with this new IM account then tap on YES and you will be redirected to merge form page of mine.

<p align="center">
  <img src="https://github.com/rahul-y/intermine-authorization-server/blob/integration/src/main/resources/static/merge_form.png" alt="Intermine Login" width="500">
</p>

Enter your old mine account credentials and click on the merge account. Once you click on it then you will redirected back to IM auth server page where you will be asked to give access of your name and email to the mine. If you allow it then you will be successfully logged in to the mine otherwise deny will not logged you in to mine

4. Cross Domain SSO

Once you logged in to any mine by IM account in your browser then you will be automatically logged in to all of the mine. If you already authorized other mines to access your name and email i.e have already an account on other mine then you will be automatically logged in to that mine if you open it in another tab.

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
