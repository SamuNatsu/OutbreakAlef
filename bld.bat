@echo off
call cln.bat
echo Building....
xcopy /S /I /Y assets build\assets
javac -encoding UTF-8 -Werror -d .\build com/Application.java
jar -cvfm OutbreakAlef.jar MANIFEST.MF -C .\build .
echo Done.
