@echo off
setlocal

REM ==============================
REM Configuration
REM ==============================

set "APP_NAME=sprint"
set "SRC_DIR=src\main\java"
set "BUILD_DIR=build"
set "LIB_DIR=lib"

REM ==============================
REM Nettoyage
REM ==============================

if exist "%BUILD_DIR%" (
    rmdir /s /q "%BUILD_DIR%"
)

mkdir "%BUILD_DIR%"

REM ==============================
REM Recherche des fichiers Java
REM ==============================

dir /s /b "%SRC_DIR%\*.java" > sources.txt

echo.
echo Fichiers Java a compiler :
type sources.txt
echo.

REM ==============================
REM Compilation
REM ==============================

javac -cp "%LIB_DIR%\*" -d "%BUILD_DIR%" @sources.txt

if errorlevel 1 (
    echo.
    echo Erreur lors de la compilation.
    del sources.txt
    exit /b 1
)

del sources.txt

REM ==============================
REM Creation du JAR
REM ==============================

cd /d "%BUILD_DIR%"

jar -cvf "%APP_NAME%.jar" *

cd /d "%~dp0"

REM ==============================
REM Deplacement du JAR
REM ==============================

move /y "%BUILD_DIR%\%APP_NAME%.jar" "%APP_NAME%.jar"

echo.
echo ==============================
echo Build termine avec succes !
echo ==============================
echo.

endlocal