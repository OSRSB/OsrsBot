@echo off
REM ============================================================
REM OsrsBot Launcher with Jagex Authentication Support
REM ============================================================
REM
REM FIRST-TIME SETUP (one-time only):
REM   1. Open "RuneLite (configure)" from Start Menu
REM      (or manually edit %LOCALAPPDATA%\RuneLite\settings.json)
REM   2. In "Client arguments", add: --insecure-write-credentials
REM   3. Open Jagex Launcher and click Play OSRS (RuneLite)
REM   4. Log in to your account, then close RuneLite
REM   5. Verify %USERPROFILE%\.runelite\credentials.properties exists
REM   6. Remove --insecure-write-credentials from client arguments
REM   7. Now you can run this script to launch the bot!
REM
REM Tokens persist until you click "End sessions" on runescape.com
REM ============================================================

set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot
set JAVA="%JAVA_HOME%\bin\java.exe"

if not exist %JAVA% (
    echo ERROR: Java 17 not found at %JAVA_HOME%
    echo Please install Eclipse Adoptium JDK 17
    pause
    exit /b 1
)

REM Load Jagex credentials as environment variables so the game client
REM can authenticate via System.getenv("JX_ACCESS_TOKEN") etc.
set CREDS=%USERPROFILE%\.runelite\credentials.properties
if exist "%CREDS%" (
    echo Loading Jagex credentials from %CREDS%
    for /f "usebackq tokens=1,* delims==" %%a in ("%CREDS%") do (
        REM Skip comments (lines starting with #)
        echo %%a | findstr /b "#" >nul 2>&1
        if errorlevel 1 (
            set "%%a=%%b"
            echo   Set env: %%a
        )
    )
) else (
    echo WARNING: No credentials.properties found!
    echo.
    echo You need to set up Jagex authentication first:
    echo   1. Open "RuneLite (configure)" from Start Menu
    echo   2. Add --insecure-write-credentials to "Client arguments"
    echo   3. Launch RuneLite via Jagex Launcher and log in
    echo   4. Close RuneLite, then run this script again
    echo.
    echo Launching without credentials - you may not be able to log in...
    echo.
)

cd /d "%~dp0"
%JAVA% -Xmx2g -XX:ReservedCodeCacheSize=512m -XX:HeapBaseMinAddress=0x10000000 -jar OSRSBot.jar --bot-runelite --developer-mode %*
