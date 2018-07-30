@echo off
call %1/../setEnv.bat
%JAVA% -jar %1/../ext/upgrade.jar update %1 >> %1/../upgrade.log
exit