SET DAEMON_HOME=%CD%\..
SET DAEMON_LOGS=%DAEMON_HOME%\logs
SET SERVICE_NAME=aum-daemon
SET BOOTSTRAP_CLASS=com.haojiankang.aum.daemon.Bootstrap
SET COMMOND_ARGS=//IS//%SERVICE_NAME% --DisplayName="%SERVICE_NAME%" --Description="%SERVICE_NAME%"
SET COMMOND_ARGS=%COMMOND_ARGS% --Startup=auto --Install=%DAEMON_HOME%\bin\aum-daemon.exe --Jvm=auto --Classpath=%DAEMON_HOME%\bin\aum-daemon.jar --StartMode=jvm
SET COMMOND_ARGS=%COMMOND_ARGS% --StartClass=%BOOTSTRAP_CLASS% --StartMethod=start --StartParams=start --StopMode=jvm --StopClass=%BOOTSTRAP_CLASS% --StopMethod=stop --StopParams=stop
SET COMMOND_ARGS=%COMMOND_ARGS% --StdOutput=auto --StdError=auto --LogPath=%DAEMON_LOGS% --LogLevel=Debug
SET EXECUTE_LINE=aum-daemon.exe %COMMOND_ARGS%
%EXECUTE_LINE%