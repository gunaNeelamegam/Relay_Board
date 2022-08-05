#!/bin/bash

JAVAFX=/usr/share/openjfx/lib/
JAR=/usr/lib/

## zenity --info --text "############\nSmartPas\n###########\n"


java --module-path $JAVAFX --add-modules javafx.controls,javafx.fxml -jar $JAR/smartpas.jar

exit 0
