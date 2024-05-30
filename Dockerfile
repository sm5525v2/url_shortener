FROM amazoncorretto:17
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/url_shortener-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} url-shortener.jar
ENTRYPOINT ["java","-jar","/url-shortener.jar"]