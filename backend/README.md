# AKAZAKI API

## Prérequis

- Java 17
- Maven
- MySQL
- Bruno (optionnel)

## Installation

### 1. Base de données

Vous avez deux options pour créer la base de données :

#### Option 1 : Via MySQL CLI
```sql
CREATE DATABASE akazaki_bdd;
```

#### Option 2 : Via phpMyAdmin
1. Connectez-vous à phpMyAdmin
2. Cliquez sur "Nouvelle base de données"
3. Entrez "akazaki_bdd" comme nom
4. Cliquez sur "Créer"

### 2. Configuration

Vérifiez la configuration dans `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/akazaki_bdd
spring.datasource.username=root
spring.datasource.password=root
```

## Lancement

1. Démarrer MySQL
2. Lancer l'application avec l'une des commandes suivantes :
   ```bash
   mvn spring-boot:run
   ```
   ou
   ```bash
   ./mvnw spring-boot:run
   ```

## Documentation

### 1. Swagger UI
Accédez à la documentation API via :
```
http://localhost:8080/api-docs
```

### 2. Bruno
- Installer Bruno depuis [le site officiel](https://www.usebruno.com/)
- Ouvrir le dossier `src/main/resources/Akazaki/`
- Choisir l'environnement "dev" en haut droite
- Les requêtes sont prêtes à l'emploi

## Profils Spring

Deux profils sont disponibles :
- `test` : Base de données en mémoire
- `prod` : Base de données MySQL

Pour changer de profil, modifiez dans `application.properties` :
```properties
spring.profiles.active=prod
```

## Tests

Pour exécuter les tests :
```bash
mvn test
```