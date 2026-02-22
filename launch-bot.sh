#!/bin/bash
# ============================================================
# OsrsBot Launcher with Jagex Authentication Support
# ============================================================
#
# FIRST-TIME SETUP (one-time only):
#   1. Open "RuneLite (configure)" from Start Menu
#      (or manually edit %LOCALAPPDATA%\RuneLite\settings.json)
#   2. In "Client arguments", add: --insecure-write-credentials
#   3. Open Jagex Launcher and click Play OSRS (RuneLite)
#   4. Log in to your account, then close RuneLite
#   5. Verify ~/.runelite/credentials.properties exists
#   6. Remove --insecure-write-credentials from client arguments
#   7. Now you can run this script!
#
# Tokens persist until you click "End sessions" on runescape.com
# ============================================================

export JAVA_HOME="/c/Program Files/Eclipse Adoptium/jdk-17.0.18.8-hotspot"
JAVA="$JAVA_HOME/bin/java.exe"

if [ ! -f "$JAVA" ]; then
    echo "ERROR: Java 17 not found at $JAVA_HOME"
    exit 1
fi

# Load Jagex credentials as environment variables so the game client
# can authenticate via System.getenv("JX_ACCESS_TOKEN") etc.
CREDS="$HOME/.runelite/credentials.properties"
if [ -f "$CREDS" ]; then
    echo "Loading Jagex credentials from $CREDS"
    while IFS='=' read -r key value; do
        # Skip comments and blank lines
        case "$key" in
            \#*|"") continue ;;
        esac
        # Trim whitespace
        key=$(echo "$key" | xargs)
        value=$(echo "$value" | xargs)
        if [ -n "$key" ] && [ -n "$value" ]; then
            export "$key=$value"
            echo "  Set env: $key"
        fi
    done < "$CREDS"
else
    echo "WARNING: No credentials.properties found at $CREDS"
    echo ""
    echo "You need to set up Jagex authentication first:"
    echo "  1. Open 'RuneLite (configure)' from Start Menu"
    echo "  2. Add --insecure-write-credentials to 'Client arguments'"
    echo "  3. Launch RuneLite via Jagex Launcher and log in"
    echo "  4. Close RuneLite, then run this script again"
    echo ""
    echo "Launching without credentials - you may not be able to log in..."
    echo ""
fi

cd "$(dirname "$0")"
"$JAVA" -Xmx2g -XX:ReservedCodeCacheSize=512m -XX:HeapBaseMinAddress=0x10000000 -jar OSRSBot.jar --bot-runelite --developer-mode "$@"
