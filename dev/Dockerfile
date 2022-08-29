ARG BUILDER_WORKDIR=/builder
ARG BUILD_WORKDIR=/build



FROM maven:3.6.3-adoptopenjdk-11 AS BUILDER

# speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

ARG BUILDER_WORKDIR
WORKDIR $BUILDER_WORKDIR

COPY ../libs $BUILDER_WORKDIR/../libs/
COPY ./dev/simulatte-core/ simulatte-core/
COPY ./dev/simulatte-core-deps/ simulatte-core-deps/
COPY ./dev/simulatte-offline/ simulatte-offline/
COPY ./dev/simulatte-online/ simulatte-online/ 

COPY ./dev/pom.xml .
RUN mvn clean install -DskipTests=true -Ponline | grep -v 'Download.*'



FROM openjdk:8-jdk-alpine

ARG BUILD_WORKDIR
WORKDIR $BUILD_WORKDIR

COPY --from=builder /builder/simulatte-online/target/simulatte-online.jar /opt/simulatte-online.jar

CMD ["/usr/bin/java", "-Xms1024M", "-Xmx4096M", "-jar", "/opt/simulatte-online.jar"]