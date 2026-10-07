#!/bin/bash

set -e

TOMCAT_HOME="${TOMCAT_HOME:-$1}"

if [ -z "$TOMCAT_HOME" ]; then
  echo "Usage: TOMCAT_HOME=/path/to/tomcat ./deploy.sh"
  echo "Exemple: TOMCAT_HOME=/home/andretseheno/apache-tomcat-10.1.14 ./deploy.sh"
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

cp -r build/main "$APP_DIR/WEB-INF/classes/"
cp -r webapp/* "$APP_DIR/"

echo "Projet déployé dans $APP_DIR"
echo "Lance Tomcat avec: $TOMCAT_HOME/bin/startup.sh"
echo "Test: http://localhost:8080/sprint/"
