@echo off

SET LOG=shkbot.log
GOTO :BuildClasspath

:BuildClasspath
FOR /R ./lib %%a in (*.jar) DO CALL :AddToPath %%a
SET CLASSPATH=%1;%CLASSPATH%bin
GOTO :StartBot

:AddToPath
SET CLASSPATH=%1;%CLASSPATH%
GOTO :EOF

:StartBot
echo Logging shkbot output to %LOG% 
echo Classpath %CLASSPATH% > %LOG%
java -Xmx512m -classpath %CLASSPATH% -splash:"data\images\splash-screen-final.png" com.middlegames.shkbot.gui.BotUI >> %LOG%
