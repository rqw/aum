@echo off
setlocal
call setEnv.bat

echo Installing the service '%SERVICE_NAME%' ...
echo Using DAEMON_HOME:    "%DAEMON_HOME%"
echo Using DAEMON_LOGS:    "%DAEMON_LOGS%"
echo Using JAVA_HOME:        "%JAVA_HOME%"


aum-daemon.exe //IS//%SERVICE_NAME% ^
    --DisplayName="%SERVICE_NAME%" ^
    --Description="%SERVICE_NAME%" ^
    --Startup=auto ^
    --Install=%DAEMON_HOME%\bin\aum-daemon.exe ^
    --Jvm=%JVM% ^
    --Classpath=%DAEMON_HOME%\bin\aum-daemon.jar ^
    --StartMode=jvm ^
    --StopMode=jvm ^
    --StartClass=com.haojiankang.aum.daemon.Bootstrap ^
    --StopClass=com.haojiankang.aum.daemon.Bootstrap ^
    --StartMethod=start ^
    --StopMethod=stop ^
    --StartParams=start ^
    --StopParams=stop ^
    --StdOutput=auto ^
    --StdError=auto ^
    --LogPath=%DAEMON_LOGS% ^
    --LogLevel=Debug ^
    --JvmOptions="-Ddaemon.home=%DAEMON_HOME%;" ^
    --JvmMs=128 ^
    --JvmMx=256
echo install service aum-daemon
endlocal
pause