@ECHO off
TITLE InsaneAion 4.8 - LoginServer Panel
color 4E
:START
CLS
IF "%MODE%" == "" (
CALL PanelLS.bat
)
ECHO Starting InsaneAion Login in %MODE% mode.
JAVA -version:"1.7" %JAVA_OPTS% -cp ./libs/*;ac-login.jar com.aionemu.loginserver.LoginServer
SET CLASSPATH=%OLDCLASSPATH%
IF ERRORLEVEL 2 GOTO START
IF ERRORLEVEL 1 GOTO ERROR
IF ERRORLEVEL 0 GOTO END
:ERROR
ECHO.
ECHO Login Server has terminated abnormaly!
ECHO.
PAUSE
EXIT
:END
ECHO.
ECHO Login Server is terminated!
ECHO.
PAUSE
EXIT