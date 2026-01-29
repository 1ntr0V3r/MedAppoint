@echo off
echo ===================================================
echo   Lancement du Tunnel Médical MedAppoint
echo ===================================================
echo.
echo Ce script rend votre serveur local accessible sur Internet.
echo.
echo 1. Si Backend n'est pas lance, lancez-le dans un autre terminal (./gradlew bootRun).
echo 2. Laissez cette fenetre ouverte tant que vous utilisez l'application.
echo.
echo NOTE IMPORTANTE :
echo Si l'URL ci-dessous change (ex: https://xyz.serveo.net),
echo vous devrez peut-etre mettre a jour l'URL dans l'application Android 
echo (via un ecran de configuration ou en modifiant RetrofitClient.java).
echo.
echo Connexion en cours...
ssh -o StrictHostKeyChecking=no -R 80:localhost:8080 serveo.net
pause
