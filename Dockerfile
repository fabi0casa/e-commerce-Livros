# Etapa 1: Build com Maven e OpenJDK 21
FROM openjdk:21-slim AS build

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o arquivo pom.xml e o código-fonte
COPY pom.xml .
COPY src ./src

# Executar o build do Maven
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM openjdk:21-slim

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o .jar gerado pela etapa anterior
COPY --from=build /app/target/livraria-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta 8080
EXPOSE 8080

# Comando para iniciar o aplicativo
CMD ["java", "-jar", "app.jar"]
