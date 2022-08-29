ARG BUILDER_WORKDIR=/builder/
ARG BUILD_WORKDIR=/build/



FROM maven:3.6.3-adoptopenjdk-11 AS BUILDER

# speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

ARG BUILDER_WORKDIR
WORKDIR $BUILDER_WORKDIR

COPY libs libs/
COPY simulatte-core/ simulatte-core/
COPY simulatte-offline/ simulatte-offline/
COPY simulatte-online/ simulatte-online/

COPY pom.xml .
RUN mvn clean install -DskipTests=true -Ponline | grep -v 'Download.*'



FROM openjdk:8-jdk-alpine

ARG BUILD_WORKDIR
WORKDIR $BUILD_WORKDIR

COPY --from=builder /builder/simulatte-online/target/simulatte-online-0.0.1-SNAPSHOT.jar ./simulation-online-service.jar

CMD ["/usr/bin/java", "-Xms1024M", "-Xmx4096M", "-jar", "simulation-online-service.jar"]