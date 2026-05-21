#!/usr/bin/env bash
# Starts a virtual X11 framebuffer then launches OsrsBot.
# Forwards SIGTERM/SIGINT to the JVM so it can shut down cleanly.
set -euo pipefail

echo "[entrypoint] Starting Xvfb on ${DISPLAY}"
Xvfb "${DISPLAY}" -screen 0 1024x768x24 -nolisten tcp &
XVFB_PID=$!

cleanup() {
    echo "[entrypoint] Shutting down…"
    kill "${XVFB_PID}" 2>/dev/null || true
}
trap cleanup SIGTERM SIGINT EXIT

echo "[entrypoint] Starting OsrsBot (LOG_LEVEL=${LOG_LEVEL})"
exec java \
    -Xmx512m \
    -Djava.awt.headless=false \
    -Duser.home="${OSRSBOT_HOME}" \
    -DLOG_LEVEL="${LOG_LEVEL}" \
    -DLOG_DIR="${LOG_DIR}" \
    -jar /app/OSRSBot.jar \
    --bot-runelite \
    "$@"
