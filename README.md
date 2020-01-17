# DeliriumReborn
Le jeu de ta vie

## Usage
### Prérequis
Vous devez avoir Java 13 installé sur votre ordinateur, et accessible via un terminal (grâce à la variable système PATH).

Il faut également ajouter Java 13 à la variable d'environnement "JAVA_HOME".

### Compiler les sources et lancer l'application
Pour compiler et lancer l'application, vous devez ouvrir un terminal dans le dossier du projet, et lancer la commande :

```bash
gradlew run          //sous Windows
./gradlew run        //sous Linux et macOS
```

### Créer un zip distribuable
Pour créer un zip distribuable (contenant des fichiers JAR), vous devez ouvrir un terminal dans le dossier du projet, et lancer la commande :

```bash
gradlew assemble          //sous Windows
./gradlew assemble        //sous Linux et macOS
```

Le zip ainsi généré se trouve dans le dossier "./build/distributions" (depuis la racine du projet). L'application requiert un JRE pour être exécutée.

### Créer un exécutable natif
Pour créer un exécutable, vous devez ouvrir un terminal dans le dossier du projet, et lancer la commande :

```bash
gradlew jlink          //sous Windows
./gradlew jlink        //sous Linux et macOS
```

L'exécutable ainsi généré se trouve dans le dossier "./build/image" (depuis la racine du projet). L'exécutable peut être exécuté, même si Java n'est pas installé sur la machine.
