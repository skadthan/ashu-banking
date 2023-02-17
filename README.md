# Online Bank Account
Spring Boot/Spring Data/Spring Security/Hibernate/MySQL/REST

The project simulates online banking system. It allows to register/login, deposit/withdraw money from accounts, add/edit recipients,
transfer money between accounts and recipients, view transactions, make appointments.

There are two roles user and admin.

The admin has there own fronent implemented in Angular2, which communicates with backend through REST services.

There is a sql dump with prepopulated data inside project folder.
The username and password for database: root

You can login with:
User - `password`
Admin - `password`

## Deployment Steps on Docker:
###### Download application
```
git clone https://github.com/skadthan/ashu-banking.git
```
###### Start MySQL Docker Container
```
docker run --detach --name=bankmysql --env="MYSQL_ROOT_PASSWORD=root" -p 3306:3306 mysql:5.7.22
```
###### Execute SQL scripts in MySQL
```
cd online-bank
docker exec -i bankmysql mysql -uroot -proot < src/main/resources/import.sql
```
###### Run Docker image of the application
```
docker run --detach -p 8800:8800 --link bankmysql:localhost -t skadthan/ashu-banking:latest
```
Access the application by clicking the URL "[http://localhost:8800!](http://localhost:8800)"

## Deployment Steps without Docker:
###### Build application
```
mvn clean build
```
###### DB Setup
 * Start [MySQL server!](https://dev.mysql.com/downloads/mysql/)
 * Use [MySQLWorkbench!](https://www.mysql.com/products/workbench/)
 * Run [SQL scripts!](https://github.com/skadthan/online-bank/blob/master/sql_dump/onlinebanking.sql) in MySQL database

###### Run application
```
java -jar target/ashu-banking-0.0.1-SNAPSHOT.jar
```

## Things to know:
###### Build Docker image for the application
```
docker build -t skadthan/ashu-banking:latest .
```
###### Create Jenkins image that has Maven
```
sudo chmod 777 /var/run/docker.sock && \
mkdir -p /jenkins_bkp/jenkins_home && \
chmod -R 777 /jenkins_bkp && \
git clone https://github.com/skadthan/ashu-banking.git && \
cd ashu-banking && \
git checkout master && \
cp Dockerfile-Jenkins-Maven ../Dockerfile && \
cd .. && \
docker build -t skadthan/jenkins-maven-docker:v0.1 .
```
###### Start Jenkins Server on Docker
```
docker run --detach -v /var/run/docker.sock:/var/run/docker.sock -v $(which docker):$(which docker) -p 9080:8080 -p 50000:50000 -v /jenkins_bkp/jenkins_home:/var/jenkins_home skadthan/jenkins-maven-docker:v0.1
```
###### Setup "online-bank" project in Jenkins:
 * Login to Jenkins and setup a pipeline project with source code from [Link to OnlineBankAccount GIT repo!](https://github.com/skadthan/ashu-banking.git)
 * Run the job to build and deploy the application

###### Debug H2 DB while testing
 * Set a debug point in any test step and check the URL "http://localhost:8800/console" while testing
