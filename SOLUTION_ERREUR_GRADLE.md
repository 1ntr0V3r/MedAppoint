# Solution pour l'erreur Gradle

## Problèmes identifiés

1. **Cache Gradle corrompu** : Le cache contient des métadonnées corrompues pour `aapt2`
2. **Version Java incorrecte** : Le projet nécessite Java 11+ mais Java 8 est actuellement utilisé

## Solutions

### Solution 1 : Utiliser le JDK d'Android Studio (Recommandé)

Android Studio inclut son propre JDK (Java 11+). Pour l'utiliser :

1. Ouvrez Android Studio
2. Allez dans **File → Settings → Build, Execution, Deployment → Build Tools → Gradle**
3. Dans **Gradle JDK**, sélectionnez le JDK embarqué d'Android Studio (généralement "jbr-17" ou similaire)
4. Cliquez sur **Apply** et **OK**

### Solution 2 : Installer Java 11 ou supérieur

1. Téléchargez Java 11+ depuis [Oracle](https://www.oracle.com/java/technologies/downloads/) ou [Adoptium](https://adoptium.net/)
2. Installez Java
3. Configurez `JAVA_HOME` dans les variables d'environnement Windows
4. Redémarrez Android Studio

### Solution 3 : Nettoyer le cache Gradle (Déjà fait)

Le cache Gradle a été nettoyé. Si le problème persiste :

1. Fermez Android Studio
2. Supprimez le dossier `.gradle` dans le projet (s'il existe)
3. Supprimez le cache Gradle : `%USERPROFILE%\.gradle\caches`
4. Rouvrez Android Studio et laissez-le re-télécharger les dépendances

### Solution 4 : Configurer Java dans gradle.properties

Si vous avez Java 11+ installé ailleurs, décommentez et modifiez cette ligne dans `gradle.properties` :

```properties
org.gradle.java.home=C:\\Chemin\\Vers\\Votre\\JDK-11
```

## Après avoir résolu le problème Java

Une fois Java 11+ configuré, exécutez dans le terminal :

```powershell
cd "C:\Users\rbami\OneDrive\Desktop\EMSI\Projects\MedAppoint"
.\gradlew clean
.\gradlew build
```

Ou utilisez Android Studio pour synchroniser le projet (File → Sync Project with Gradle Files).

## Vérification

Pour vérifier que Java 11+ est utilisé :

```powershell
java -version
```

Vous devriez voir une version 11 ou supérieure.


