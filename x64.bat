cmd /c mvn clean package -Dmaven.test.skip=true
RD /S /Q target
mkdir target\dist\bin
copy aum-daemon\svc\* target\dist\bin
mkdir target\dist\bin\ext
copy aum-exec\target\aum-exec-1.0-SNAPSHOT.jar target\dist\bin\ext\upgrade.jar
copy aum-daemon\target\aum-daemon-1.0-SNAPSHOT.jar target\dist\bin\aum-daemon.jar
copy aum-daemon\src\main\resources\* target\dist\bin
del /Q target\dist\bin\aum-daemon.exe
copy aum-daemon\svc\x64\aum-daemon.exe target\dist\bin
