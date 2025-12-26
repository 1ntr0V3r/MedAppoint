# Instructions pour résoudre les erreurs Gradle

## Problème actuel
Le cache Gradle est corrompu et des fichiers de métadonnées sont manquants.

## ✅ Actions déjà effectuées
- Cache Gradle utilisateur nettoyé
- Cache Gradle du projet nettoyé
- Dossiers de build locaux supprimés
- Daemons Gradle arrêtés

## ⚠️ Problème Java à résoudre

**Votre système utilise Java 8, mais le projet nécessite Java 11+**

### Solution 1 : Utiliser le JDK d'Android Studio (RECOMMANDÉ)

1. **Ouvrez Android Studio**
2. Allez dans **File → Settings** (ou `Ctrl+Alt+S`)
3. Naviguez vers **Build, Execution, Deployment → Build Tools → Gradle**
4. Dans la section **Gradle JDK**, sélectionnez :
   - **jbr-17** (JetBrains Runtime 17) - RECOMMANDÉ
   - ou **jbr-11** si disponible
   - ou **Embedded JDK** (le JDK embarqué d'Android Studio)
5. Cliquez sur **Apply** puis **OK**
6. Allez dans **File → Sync Project with Gradle Files** (ou `Ctrl+Shift+O`)

### Solution 2 : Installer Java 11+ manuellement

1. Téléchargez Java 17 LTS depuis [Adoptium](https://adoptium.net/temurin/releases/)
2. Installez Java
3. Configurez la variable d'environnement `JAVA_HOME` :
   - Ouvrez "Variables d'environnement" dans Windows
   - Créez/modifiez `JAVA_HOME` pour pointer vers le JDK installé
   - Exemple : `C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot`
4. Redémarrez Android Studio

### Solution 3 : Configurer dans gradle.properties

Si vous avez Java 11+ installé ailleurs, décommentez et modifiez cette ligne dans `gradle.properties` :

```properties
org.gradle.java.home=C:\\Chemin\\Vers\\Votre\\JDK-11
```

## 🔄 Après avoir configuré Java 11+

Une fois Java 11+ configuré, suivez ces étapes :

### Option A : Via Android Studio (RECOMMANDÉ)
1. **File → Sync Project with Gradle Files**
2. Attendez que Gradle télécharge toutes les dépendances
3. Si des erreurs persistent, allez dans **File → Invalidate Caches / Restart**

### Option B : Via ligne de commande

```powershell
# Dans le dossier du projet
cd "C:\Users\rbami\OneDrive\Desktop\EMSI\Projects\MedAppoint"

# Vérifier la version Java
java -version

# Nettoyer et construire
.\gradlew clean
.\gradlew build
```

## 🧹 Si le problème persiste

Exécutez le script de nettoyage :

```powershell
.\clean-gradle.ps1
```

Puis dans Android Studio :
1. **File → Invalidate Caches / Restart**
2. Cochez toutes les options
3. Cliquez sur **Invalidate and Restart**
4. Attendez le redémarrage
5. **File → Sync Project with Gradle Files**

## ✅ Vérification

Pour vérifier que tout fonctionne :

1. Ouvrez Android Studio
2. **File → Sync Project with Gradle Files**
3. Vérifiez qu'il n'y a pas d'erreurs dans la barre de synchronisation en bas
4. Essayez de construire le projet : **Build → Make Project** (`Ctrl+F9`)

## 📝 Notes importantes

- **Ne modifiez PAS** le fichier `local.properties` - il est généré automatiquement par Android Studio
- Le cache Gradle sera automatiquement régénéré lors de la première synchronisation
- La première synchronisation peut prendre plusieurs minutes car Gradle doit télécharger toutes les dépendances

