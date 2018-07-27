@echo off
java -jar %1/../ext/upgrade.jar update %1 >> %1/../upgrade.log
exit