ARG BUILD_IMAGE=alpine:3.21.3
FROM ${BUILD_IMAGE} AS build

RUN apk add openjdk17

WORKDIR /app
COPY .mvn .mvn
COPY pom.xml mvnw ./
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw package -DskipTests

ARG EXTRACTOR_IMAGE=alpine:3.21.3
FROM ${EXTRACTOR_IMAGE} AS extractor

RUN apk add openjdk17

WORKDIR /app
COPY --from=build /app/target/advertising-service-*.jar advertising-service-app.jar
RUN java -Djarmode=layertools -jar advertising-service-app.jar extract

ARG RESULT_IMAGE=alpine:3.21.3
FROM ${RESULT_IMAGE} AS result

RUN apk add openjdk17

WORKDIR /app
COPY --from=extractor /app/dependencies/ ./
COPY --from=extractor /app/spring-boot-loader/ ./
COPY --from=extractor /app/snapshot-dependencies/ ./
COPY --from=extractor /app/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
