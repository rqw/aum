SET JAVA_HOME=C:\hjksoft\jdk\1.8
SET JAVA_OPTIONS=-Ddaemon.home=%DAEMON_HOME%;
SET DAEMON_HOME=%CD%\..
SET DAEMON_LOGS=%DAEMON_HOME%\logs
SET JAVA=%JAVA_HOME%\bin\java.exe
SET SERVICE_NAME=aum-daemon
SET JVM=%JAVA_HOME%\jre\bin\server\jvm.dll