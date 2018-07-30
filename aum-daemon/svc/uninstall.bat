@echo off
call setEnv.bat
aum-daemon.exe //DS//%SERVER_NAME%
echo uninstall service aum-daemon
pause