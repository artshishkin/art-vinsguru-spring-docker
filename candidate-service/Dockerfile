FROM bellsoft/liberica-openjdk-alpine:11.0.16 as builder

WORKDIR /usr/share/app

COPY target/*.jar app.jar

RUN java -Djarmode=layertools -jar app.jar extract

FROM bellsoft/liberica-openjdk-alpine:11.0.16

ENV SERVER_PORT 8080

EXPOSE 8080

RUN apk add --update curl && \
    apk add jq && \
    rm -rf /var/cache/apk/*

HEALTHCHECK  --interval=2s --timeout=1s --start-period=5s --retries=30 \
    CMD curl -f localhost:8080/actuator/health | jq --exit-status -n 'inputs | if has("status") then .status=="UP" else false end' > /dev/null || exit 1

WORKDIR /usr/share/app
COPY --from=builder /usr/share/app/dependencies/ ./
COPY --from=builder /usr/share/app/spring-boot-loader/ ./
COPY --from=builder /usr/share/app/snapshot-dependencies/ ./
COPY --from=builder /usr/share/app/application/ ./
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]
