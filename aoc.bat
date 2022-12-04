@echo off

set year=%1%
set day=%2%

set GROOVY_LIB_DIR=%GROOVY_HOME%/lib
set CP=.;./util;%GROOVY_LIB_DIR%/*;%GROOVY_LIB_DIR%/extras-jaxb/*

java -cp "%CP%" groovy.ui.GroovyMain %year%\day%day%.groovy