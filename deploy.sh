#!/bin/bash
set -e

TOMCAT_HOME="${TOMCAT_HOME:-$1}"

if [ -z "$TOMCAT_HOME" ]; then
  echo "Usage: TOMCAT_HOME=/path/to/tomcat ./deploy.sh"
  echo "Exemple: TOMCAT_HOME=/home/andretseheno/Logiciel/apache-tomcat-10.1.28 ./deploy.sh"
  exit 1
fi

if [ ! -f "$TOMCAT_HOME/bin/catalina.sh" ]; then
  echo "Erreur: $TOMCAT_HOME/bin/catalina.sh introuvable"
  exit 1
fi

cd "$(dirname "$0")"

bash build.sh

APP_DIR="$TOMCAT_HOME/webapps/sprint"
rm -rf "$APP_DIR"
mkdir -p "$APP_DIR/WEB-INF/classes"
mkdir -p "$APP_DIR/WEB-INF/lib"

# Classes compilées
cp -r build/main "$APP_DIR/WEB-INF/classes/"

# Pages web (WEB-INF/views, web.xml, ...)
cp -r webapp/* "$APP_DIR/"

# Librairies (sans servlet-api, fourni par Tomcat)
for jar in lib/*.jar; do
  [ -e "$jar" ] || continue
  case "$(basename "$jar")" in
    servlet-api*.jar) continue ;;
  esac
  cp "$jar" "$APP_DIR/WEB-INF/lib/"
done

echo "Projet déployé dans $APP_DIR"
echo "Librairies : $(ls "$APP_DIR/WEB-INF/lib" | tr '\n' ' ')"
echo "Lance Tomcat avec: $TOMCAT_HOME/bin/startup.sh"
echo "Test: http://localhost:8080/sprint/"