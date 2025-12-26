# Script de nettoyage complet du cache Gradle
Write-Host "Nettoyage du cache Gradle..." -ForegroundColor Yellow

# Arrêter tous les daemons Gradle
Write-Host "Arrêt des daemons Gradle..." -ForegroundColor Cyan
& "$PSScriptRoot\gradlew" --stop 2>$null

# Supprimer le cache utilisateur Gradle
Write-Host "Suppression du cache utilisateur Gradle..." -ForegroundColor Cyan
Remove-Item -Path "$env:USERPROFILE\.gradle\caches" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "$env:USERPROFILE\.gradle\daemon" -Recurse -Force -ErrorAction SilentlyContinue

# Supprimer les dossiers de build locaux
Write-Host "Suppression des dossiers de build locaux..." -ForegroundColor Cyan
Remove-Item -Path "$PSScriptRoot\.gradle" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "$PSScriptRoot\build" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "$PSScriptRoot\app\build" -Recurse -Force -ErrorAction SilentlyContinue

Write-Host "Nettoyage terminé!" -ForegroundColor Green
Write-Host ""
Write-Host "IMPORTANT: Assurez-vous d'avoir Java 11+ configuré avant de reconstruire le projet." -ForegroundColor Yellow
Write-Host "Dans Android Studio: File -> Settings -> Build Tools -> Gradle -> Gradle JDK" -ForegroundColor Yellow

