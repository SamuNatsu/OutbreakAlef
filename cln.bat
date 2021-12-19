@echo off
echo Deleting .class files...
for /r %%s in (*.class) do del %%s
echo Done.
