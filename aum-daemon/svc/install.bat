SET DAEMON_HOME=%CD%\..
SET DAEMON_LOGS=%DAEMON_HOME%\logs
aum-daemon.exe //IS//aum-daemon --DisplayName="aum-daemon" --Description="aum-daemon" --Startup=auto --Install=%DAEMON_HOME%\bin\aum-daemon.exe --Jvm=auto --Classpath=%DAEMON_HOME%\bin\aum-daemon.jar --StartMode=jvm --StartClass=com.haojiankang.aum.daemon.Bootstrap --StartMethod=start --StartParams=start --StopMode=jvm --StopClass=com.haojiankang.aum.daemon.Bootstrap --StopMethod=stop --StopParams=stop --StdOutput=auto --StdError=auto --LogPath=%DAEMON_LOGS% --LogLevel=Debug




