FROM eclipse-temurin:17-alpine
RUN addgroup -S devsu && adduser -S admin -G devsu
USER admin:devsu
VOLUME /tmp
ARG JAR_FILE=/target/cuentadevsu-0.0.1.jar
COPY ${JAR_FILE} app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]