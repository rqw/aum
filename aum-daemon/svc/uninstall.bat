@echo off
call setEnv.bat
aum-daemon.exe //DS//%SERVICE_NAME%
echo uninstall service aum-daemon
pause