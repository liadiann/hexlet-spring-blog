FROM gradle:8.12-jdk21

WORKDIR /app

COPY . .

RUN ./gradlew build

CMD ["java", "-jar", "build/libs/hexlet-spring-blog-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=production"]