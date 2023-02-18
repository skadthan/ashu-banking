#FROM java:8 
#FROM openjdk
#FROM openjdk:8u181-jdk
#FROM maven:3.6.3-jdk-8

#add back
#FROM maven

## Install maven
#RUN apt-get update
#RUN apt-get install -y maven

#add back
#WORKDIR /code 

# Prepare by downloading dependencies
#add back
#ADD pom.xml /code/pom.xml

#RUN ["mvn", "dependency:resolve"]
# RUN ["mvn", "verify"]

#add back
# Adding source, compile and package into a fat jar
#ADD src /code/src
#RUN ["mvn", "package", "-DskipTests"]
#add back
#EXPOSE 8080
#CMD ["java", "-jar", "target/ashu-banking-0.0.1-SNAPSHOT.jar"]




FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ashu-banking.jar
RUN sh -c 'touch /ashu-banking.jar'
ENV JAVA_OPTS=""
EXPOSE 8080
CMD [ "java", "-jar" , "/ashu-banking.jar" ]
