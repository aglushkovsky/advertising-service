FROM alpine:3.21.3 AS build

RUN apk add openjdk17

WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

FROM alpine:3.21.3 AS result

RUN apk add openjdk17

WORKDIR /app
COPY --from=build /app/target/advertising-service-*.jar advertising-service-app.jar

ENTRYPOINT ["java", "-jar", "advertising-service-app.jar"]
