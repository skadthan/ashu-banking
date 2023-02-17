#FROM java:8 
#FROM openjdk

FROM maven

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
