@echo off

set GROOVY_LIB_DIR=%GROOVY_HOME%/lib
set CP=.;./util;%GROOVY_LIB_DIR%/*

java -cp "%CP%" groovy.ui.GroovyMain day%1%.groovy