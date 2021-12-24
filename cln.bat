@echo off
echo Cleaning...
if exist build rmdir /s /q build
if exist *.jar del *.jar
echo Done.
