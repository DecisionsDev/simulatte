FROM maven:3.6.3-adoptopenjdk-11 AS builder

# speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

ENV WORKDIR /build

RUN mkdir ${WORKDIR}
COPY pom.xml ${WORKDIR}
COPY ./src ${WORKDIR}/src

RUN cd ${WORKDIR} && mvn clean install -Dmaven.test.skip=true | grep -v 'Download.*'
RUN ls -la /build/target/

FROM openjdk:8-jdk-alpine

COPY --from=builder /build/target/simulatte-0.0.1-SNAPSHOT.jar /workdir/simulation-service.jar

CMD ["/usr/bin/java", "-Xms1024M", "-Xmx4096M", "-jar", "/workdir/simulation-service.jar"]