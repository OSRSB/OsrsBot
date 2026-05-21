FROM eclipse-temurin:17-jre-jammy

# Xvfb provides a virtual X11 display so the RuneLite canvas renders off-screen
RUN apt-get update && apt-get install -y --no-install-recommends \
        xvfb \
        libxi6 \
        libxtst6 \
        libxrender1 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY OSRSBot.jar ./
COPY src/main/resources/logback.xml ./
COPY docker/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# --- Runtime configuration via environment variables ---
# LOG_LEVEL   : logback root level (default INFO)
# LOG_DIR     : where log files are written (default /data/logs)
# OSRSBOT_HOME: user.home substitute used by AccountStore / key file
ENV DISPLAY=:99 \
    LOG_LEVEL=INFO \
    LOG_DIR=/data/logs \
    OSRSBOT_HOME=/data

RUN mkdir -p /data/logs /data/scripts /data/precompiled

VOLUME ["/data"]

ENTRYPOINT ["/entrypoint.sh"]
