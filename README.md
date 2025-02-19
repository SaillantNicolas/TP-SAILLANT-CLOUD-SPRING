# Cabinet Médical - Application de Microservices

Un système de gestion de cabinet médical implémenté avec une architecture microservices, utilisant Spring Cloud, Resilience4j, Eureka et Swagger.

## Architecture

Le projet est composé des microservices suivants:
- **Eureka Server** : Service de découverte des microservices
- **Gateway Service** : Point d'entrée central qui route les requêtes
- **Patient Service** : Gestion des patients
- **Practitioner Service** : Gestion des praticiens

## Prérequis

- Java 11
- Maven 3.6+
- Docker

## Structure du projet

```
medical-office-management/
├── eureka-server/
├── gateway-service/
├── patient-service/
├── practitioner-service/
├── pom.xml
└── README.md
```

## Installation et démarrage sans Docker

### 1. Cloner le projet

```bash
git clone https://github.com/SaillantNicolas/TP-SAILLANT-CLOUD-SPRING.git
cd TP-SAILLANT-CLOUD-SPRING
```

### 2. Démarrer les services dans l'ordre

```bash
# Démarrer Eureka Server
cd eureka-server
mvn clean install -DskipTests
mvn spring-boot:run

# Démarrer Gateway Service (dans un nouveau terminal)
cd gateway-service
mvn clean install -DskipTests
mvn spring-boot:run

# Démarrer Patient Service (dans un nouveau terminal)
cd patient-service
mvn clean install -DskipTests
mvn spring-boot:run

# Démarrer Practitioner Service (dans un nouveau terminal)
cd practitioner-service
mvn clean install -DskipTests
mvn spring-boot:run
```

## Installation et démarrage avec Docker

### 1. Construire les images Docker

```bash

# Construire les images Docker
cd eureka-server
mvn clean install -DskipTests
docker build -t medical/eureka-server:latest .

cd ../gateway-service
mvn clean install -DskipTests
docker build -t medical/gateway-service:latest .

cd ../patient-service
mvn clean install -DskipTests
docker build -t medical/patient-service:latest .

cd ../practitioner-service
mvn clean install -DskipTests
docker build -t medical/practitioner-service:latest .
```

### 2. Exécuter les conteneurs Docker

```bash
# Démarrer Eureka Server
docker run -d -p 8761:8761 --name eureka-server medical/eureka-server:latest

# Démarrer Gateway Service
docker run -d -p 8080:8080 --name gateway-service --link eureka-server:eureka-server -e "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/" medical/gateway-service:latest

# Démarrer Patient Service
docker run -d -p 8081:8081 --name patient-service --link eureka-server:eureka-server -e "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/" medical/patient-service:latest

# Démarrer Practitioner Service
docker run -d -p 8082:8082 --name practitioner-service --link eureka-server:eureka-server -e "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/" medical/practitioner-service:latest
```

## Vérification du fonctionnement

### 1. Interface Eureka
Accédez à http://localhost:8761 pour vérifier que tous les services sont enregistrés.

### 2. Swagger UI
- Patient Service: http://localhost:8081/swagger-ui.html
- Practitioner Service: http://localhost:8082/swagger-ui.html

### 3. API Gateway
Toutes les requêtes peuvent être acheminées via la passerelle:
- http://localhost:8080/api/patients
- http://localhost:8080/api/practitioners

## Test des fonctionnalités de résilience

Pour tester le circuit breaker et les mécanismes de retry:
1. Arrêtez un des services (par exemple, le service Patient)
2. Faites une requête via la passerelle vers ce service
3. Observez la réponse de fallback
