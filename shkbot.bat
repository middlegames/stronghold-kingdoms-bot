@echo off

IF NOT EXIST data\village (
    echo Error: data\village directory is missing
    GOTO :EOF
)
IF NOT EXIST data\parish (
    echo Error: data\parish directory is missing
    GOTO :EOF
)

SET LOG=bot.log
GOTO :BuildClasspath

:BuildClasspath
FOR /R ./lib %%a in (*.jar) DO CALL :AddToPath %%a
SET CLASSPATH=%1;%CLASSPATH%bin
GOTO :StartBot

:AddToPath
SET CLASSPATH=%1;%CLASSPATH%
GOTO :EOF

:Compile
echo Compiling shkbot sources...
md bin
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% -d bin -source 7 -sourcepath src\main\java src\main\java\com\middlegames\shkbot\gui\BotUI.java -verbose 1>&2 2> compilation.log
echo Compiled
GOTO :StartBot

:StartBot
IF NOT EXIST bin GOTO Compile
echo Logging shkbot output to %LOG% 
echo Classpath %CLASSPATH% > %LOG%
java -Xmx512m -classpath "%CLASSPATH%" -splash:"data\images\splash-screen-final.png" com.middlegames.shkbot.gui.BotUI >> %LOG%
