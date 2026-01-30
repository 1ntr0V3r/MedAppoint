@echo off
echo ===================================================
echo   Lancement du Backend MedAppoint (Spring Boot)
echo ===================================================
echo.
echo Please wait...
set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
call ./gradlew bootRun
pause
