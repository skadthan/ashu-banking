#FROM java:8 
#FROM openjdk

FROM maven


FROM puckel/docker-airflow:1.10.9

# install Java
USER root
RUN echo "deb http://security.debian.org/debian-security stretch/updates main" >> /etc/apt/sources.list                                                   
RUN mkdir -p /usr/share/man/man1
#RUN apt-get update -y
RUN apt-get install -y openjdk-8-jdk

RUN apt-get install unzip -y
RUN apt-get autoremove -y

USER airflow

## Install maven
#RUN apt-get update
#RUN apt-get install -y maven

WORKDIR /code

# Prepare by downloading dependencies
ADD pom.xml /code/pom.xml
#RUN ["mvn", "dependency:resolve"]
# RUN ["mvn", "verify"]

# Adding source, compile and package into a fat jar
ADD src /code/src
RUN ["mvn", "package", "-DskipTests"]

EXPOSE 8080
CMD ["java", "-jar", "target/ashu-banking-0.0.1-SNAPSHOT.jar"]
