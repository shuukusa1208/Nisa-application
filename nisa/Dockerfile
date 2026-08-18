# ビルド用ステージ
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 実行用ステージ
FROM eclipse-temurin:17-jdk
WORKDIR /app
# target フォルダ内の *.jar または *.war を検索して app.jar / app.war としてコピー
COPY --from=build /app/target/nisa-*.??? app.file
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.file"]