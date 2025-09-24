# FooBarQuix Kata (Spring Boot + Spring Batch)

## 📌 Description
Ce projet est  développé en **Java / Spring Boot / Spring Batch**.  
Il implémente les règles de transformation suivantes pour un nombre entre 0 et 100 :

- Si le nombre est **divisible par 3** ou **contient un `3`** → ajouter `"FOO"`
- Si le nombre est **divisible par 5** ou **contient un `5`** → ajouter `"BAR"`
- Si le nombre **contient un `7`** → ajouter `"QUIX"`
- La règle **"divisible par"** est prioritaire sur **"contient"**
- Les chiffres sont analysés de gauche à droite
- Si aucune règle ne s’applique → renvoyer le nombre en chaîne

👉 Exemple :

    1 -> "1"
    3 -> "FOOFOO"
    5 -> "BARBAR"
    7 -> "QUIX"
    15 -> "FOOBARBAR"
    53 -> "BARFOO"

## ⚙️ Prérequis
- Java 17+
- Maven 3.8+

## 🚀 Démarrage du projet
1. Cloner le dépôt :
   ```bash
   git clone https://github.com/marouaneza/Kata-FOO-BAR-QUIX.git
   cd Kata-FOO-BAR-QUIX
   
2. Compiler et démarrer :
   ```bash
   mvn clean spring-boot:run
   
## 🗂️ Fonctionnalités

### 1. Batch automatique au démarrage :

   Au lancement de l’application, un job Spring Batch s’exécute automatiquement.

### 2. Lancer le batch via un endpoint REST:

* Sans paramètre (utilise le fichier input.txt par défaut)
```text
 POST http://localhost:8080/api/jobs/run
```
   
Réponse :
```text
" Job lancé avec l’ID : 1, statut : COMPLETED"
```

* Avec paramètre inputFile (pour traiter un fichier personnalisé)
```text  
POST "http://localhost:8080/api/jobs/run?inputFile=/tmp/custom-input.txt"
```

👉 Le fichier `/tmp/custom-input.txt` doit contenir une liste de nombres (un par ligne).


### 3. Transformer directement un nombre via l’API REST

```text  
GET http://localhost:8080/api/transform/53
```
Réponse :
```text 
"BARFOO"
``` 
## 📄 Les fichier input/output
###  1. Fichier d'entrée (input.txt):
Le batch lit par défaut le fichier d’entrée suivant :
```text 
src/main/resources/input.txt
``` 
Ce fichier doit contenir une liste de nombres (un par ligne)

Vous pouvez changer l’emplacement du fichier d'entrée via la propriété application.properties
```properties
app.input.file=/chemin/vers/mon/input.txt
```
Vous pouvez aussi lancer le job via l’endpoint REST en spécifiant un fichier d’entrée différent
```text
POST "http://localhost:8080/api/jobs/run?inputFile=/tmp/custom-input.txt"
``` 
Si aucun fichier n’est précisé, le batch utilisera l'emplacement précisé dans application.properties sinon s'il existe pas il utilisera par défaut src/main/resources/input.txt

###  2. Fichier de sortie (output.txt):

Vous pouvez **changer l’emplacement du fichier de sortie** via la propriété `application.properties` :
```properties
app.output.file=/chemin/vers/mon/output.txt
```
Si la propriété **app.output.file** n’est pas renseignée, le fichier sera automatiquement créé dans le dossier temporaire du système (java.io.tmpdir).