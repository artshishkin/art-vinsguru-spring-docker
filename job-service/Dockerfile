FROM bellsoft/liberica-openjdk-alpine:11.0.16

WORKDIR /usr/share/app

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]