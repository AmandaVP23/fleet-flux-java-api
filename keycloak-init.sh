#!/bin/bash

set -e
KCADM=/opt/keycloak/bin/kcadm.sh
CLIENT_ID=my-admin-client-test

$KCADM config credentials --server http://keycloak-test:8080 \
  --realm master --user admin --password admin

# only needed if you hit ssl_required from the host
$KCADM update realms/master -s sslRequired=NONE

if [ -z "$($KCADM get clients -r master -q clientId=$CLIENT_ID --fields id --format csv --noquotes)" ]; then
  $KCADM create clients -r master \
    -s clientId=$CLIENT_ID \
    -s secret=test-secret \
    -s enabled=true \
    -s publicClient=false \
    -s serviceAccountsEnabled=true \
    -s standardFlowEnabled=false \
    -s directAccessGrantsEnabled=false
  $KCADM add-roles -r master \
    --uusername service-account-$CLIENT_ID --rolename admin
fi
echo "Keycloak test setup done"