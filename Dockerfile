# --- ESTÁGIO 1: Build ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas o arquivo de dependências primeiro
COPY pom.xml .

# Baixa as dependências. Se o pom.xml não mudar, o Docker usará o cache aqui!
RUN mvn dependency:go-offline

# Agora copia o código fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests

# --- ESTÁGIO 2: Runtime ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia apenas o arquivo .jar gerado no estágio anterior (muito mais leve)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]