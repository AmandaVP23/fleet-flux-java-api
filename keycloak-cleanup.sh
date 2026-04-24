o#!/usr/bin/env bash
set -euo pipefail

# ─── Configuration ────────────────────────────────────────────────────────────
KEYCLOAK_URL="${KEYCLOAK_URL:-http://localhost:8081}"
ADMIN_USER="${ADMIN_USER:-admin}"
ADMIN_PASS="${ADMIN_PASS:-admin}"

# Realms to keep (space-separated)
PROTECTED_REALMS=("master" "fleet-flux-admin")

# ─── Helpers ──────────────────────────────────────────────────────────────────
log()  { echo "[INFO]  $*"; }
warn() { echo "[WARN]  $*" >&2; }
die()  { echo "[ERROR] $*" >&2; exit 1; }

is_protected() {
  local realm="$1"
  for protected in "${PROTECTED_REALMS[@]}"; do
    [[ "$realm" == "$protected" ]] && return 0
  done
  return 1
}

# ─── Dependency check ─────────────────────────────────────────────────────────
for cmd in curl jq; do
  command -v "$cmd" &>/dev/null || die "'$cmd' is required but not installed. Run: brew install $cmd"
done

# ─── Authenticate ─────────────────────────────────────────────────────────────
log "Authenticating against $KEYCLOAK_URL ..."

TOKEN_RESPONSE=$(curl -sf \
  -X POST "${KEYCLOAK_URL}/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=admin-cli" \
  -d "username=${ADMIN_USER}" \
  -d "password=${ADMIN_PASS}" \
  -d "grant_type=password") || die "Failed to authenticate. Check KEYCLOAK_URL, ADMIN_USER, ADMIN_PASS."

ACCESS_TOKEN=$(echo "$TOKEN_RESPONSE" | jq -r '.access_token')
[[ -z "$ACCESS_TOKEN" || "$ACCESS_TOKEN" == "null" ]] && die "Could not extract access token."

log "Authenticated successfully."

# ─── Fetch all realms ─────────────────────────────────────────────────────────
log "Fetching realm list..."

REALMS=$(curl -sf \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  "${KEYCLOAK_URL}/admin/realms" | jq -r '.[].realm') \
  || die "Failed to fetch realms."

# ─── Delete unprotected realms ────────────────────────────────────────────────
DELETED=0
SKIPPED=0

while IFS= read -r realm; do
  if is_protected "$realm"; then
    log "Skipping protected realm: '$realm'"
    (( SKIPPED++ )) || true
    continue
  fi

  log "Deleting realm: '$realm' ..."
  HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
    -X DELETE \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    "${KEYCLOAK_URL}/admin/realms/${realm}")

  if [[ "$HTTP_STATUS" == "204" ]]; then
    log "  ✓ Deleted '$realm'"
    (( DELETED++ )) || true
  else
    warn "  ✗ Failed to delete '$realm' (HTTP $HTTP_STATUS)"
  fi
done <<< "$REALMS"

# ─── Summary ──────────────────────────────────────────────────────────────────
echo ""
echo "──────────────────────────────────"
echo "  Done. Deleted: $DELETED  |  Skipped (protected): $SKIPPED"
echo "──────────────────────────────────"