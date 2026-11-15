# Official OSRM image + the tools needed to download and merge OpenStreetMap extracts.
FROM ghcr.io/project-osrm/osrm-backend:latest

RUN apt-get update \
    && apt-get install -y --no-install-recommends wget ca-certificates osmium-tool \
    && rm -rf /var/lib/apt/lists/*
