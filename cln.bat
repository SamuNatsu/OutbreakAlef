@echo off
echo Cleaning...
cd build
for /r %%s in (*.*) do del %%s
cd ..
if exist *.jar del *.jar
echo Done.
