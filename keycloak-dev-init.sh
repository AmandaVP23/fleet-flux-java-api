#!/bin/bash

set -e
KCADM=/opt/keycloak/bin/kcadm.sh
CLIENT_ID=master-admin

CLIENT_SECRET="${KEYCLOAK_MASTER_CLIENT_SECRET:-test-secret}"

echo $CLIENT_SECRET

$KCADM config credentials --server http://keycloak:8080 \
  --realm master --user admin --password admin

# only needed if you hit ssl_required from the host
$KCADM update realms/master -s sslRequired=NONE

if [ -z "$($KCADM get clients -r master -q clientId=$CLIENT_ID --fields id --format csv --noquotes)" ]; then
  $KCADM create clients -r master \
    -s clientId=$CLIENT_ID \
    -s secret=$CLIENT_SECRET \
    -s enabled=true \
    -s publicClient=false \
    -s serviceAccountsEnabled=true \
    -s standardFlowEnabled=false \
    -s directAccessGrantsEnabled=false
  $KCADM add-roles -r master \
    --uusername service-account-$CLIENT_ID --rolename admin
fi
echo "Keycloak test setup done"