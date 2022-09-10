FROM bellsoft/liberica-openjdk-alpine:11.0.16

WORKDIR /usr/share/app

EXPOSE 8090

RUN apk add --update jq && \
    rm -rf /var/cache/apk/*

HEALTHCHECK  --interval=2s --timeout=1s --start-period=5s --retries=30 \
    CMD wget localhost:8090/actuator/health -q -O - | jq --exit-status -n 'inputs | if has("status") then .status=="UP" else false end' > /dev/null || exit 1

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
