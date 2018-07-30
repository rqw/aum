@echo off
call setEnv.bat
aum-daemon.exe //IS//%SERVER_NAME% --DisplayName="%SERVER_NAME%" --Description="%SERVER_NAME%" --Startup=auto --Install=%DAEMON_HOME%\bin\aum-daemon.exe --Jvm=auto --Classpath=%DAEMON_HOME%\bin\aum-daemon.jar --StartMode=jvm --StartClass=%CLASS% --StartMethod=start --StartParams=start --StopMode=jvm --StopClass=%CLASS% --StopMethod=stop --StopParams=stop --StdOutput=auto --StdError=auto --LogPath=%DAEMON_LOGS% --LogLevel=Debug
echo install service aum-daemon
pause