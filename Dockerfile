# Étape 1 : Construire l'application avec Maven
FROM maven:latest as builder

# Définition le répertoire de travail dans le conteneur
WORKDIR /app

# Copie des fichiers de configuration et les dépendances de votre application
COPY pom.xml .

# Construction du projet avec Maven (exemple)
RUN mvn clean package -DskipTests


# Étape 2 : Exécuter l'application avec Java
FROM openjdk:11-jdk

WORKDIR /app

# Copier le fichier JAR dans l'image Docker (version générique)
COPY --from=builder /app/target/velibdata-kafka-produce-*.jar  ./velibdata-kafka-produce.jar

CMD ["java", "-jar", "velibdata-kafka-produce.jar"]
