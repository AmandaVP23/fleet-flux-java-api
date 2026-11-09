#!/bin/bash
# Prepares the OSRM routing data (MLD) inside Docker, only if it does not exist yet.
# OSRM datasets are not portable across platforms, so they are always built on the
# platform that serves them.
#
# Env:
#   OSRM_DATASET  name of the dataset (default: portugal-spain)
#   OSRM_REGIONS  space-separated Geofabrik regions under /europe (default: "portugal spain")
set -euo pipefail

DATA_DIR=/data
NAME="${OSRM_DATASET:-portugal-spain}"
REGIONS="${OSRM_REGIONS:-portugal spain}"
MARKER="$DATA_DIR/$NAME.done"

if [ -f "$MARKER" ]; then
    echo "[osrm-init] Dataset '$NAME' already prepared, nothing to do."
    exit 0
fi

echo "[osrm-init] Dataset '$NAME' not found. Preparing it (this takes a while)..."

# Remove leftovers of an interrupted run (the marker is only written at the very end).
rm -f "$DATA_DIR/$NAME".osrm* "$DATA_DIR/$NAME.osm.pbf"

DOWNLOADS="$DATA_DIR/downloads"
mkdir -p "$DOWNLOADS"

FILES=()
for region in $REGIONS; do
    file="$DOWNLOADS/$region-latest.osm.pbf"
    echo "[osrm-init] Downloading $region..."
    wget --continue --no-verbose --output-document="$file" \
        "https://download.geofabrik.de/europe/$region-latest.osm.pbf"
    FILES+=("$file")
done

if [ "${#FILES[@]}" -gt 1 ]; then
    echo "[osrm-init] Merging ${#FILES[@]} extracts..."
    osmium merge "${FILES[@]}" --overwrite -o "$DATA_DIR/$NAME.osm.pbf"
else
    cp "${FILES[0]}" "$DATA_DIR/$NAME.osm.pbf"
fi

echo "[osrm-init] osrm-extract..."
osrm-extract -p /opt/car.lua "$DATA_DIR/$NAME.osm.pbf"
echo "[osrm-init] osrm-partition..."
osrm-partition "$DATA_DIR/$NAME.osrm"
echo "[osrm-init] osrm-customize..."
osrm-customize "$DATA_DIR/$NAME.osrm"

# Free disk space: the source files are not needed once the dataset is built.
rm -rf "$DOWNLOADS" "$DATA_DIR/$NAME.osm.pbf"

touch "$MARKER"
echo "[osrm-init] Done."
