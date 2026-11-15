#!/usr/bin/env bash
# usage: ./scripts/gen-cert-leaf.sh <name> <client|server>
# ./scripts/gen-cert-leaf.sh device-123 client
set -euo pipefail

# Always run from the repo root, so certs/ resolves wherever this is called from
cd "$(dirname "${BASH_SOURCE[0]}")/.."

NAME=$1; KIND=$2
EKU=$([ "$KIND" = "server" ] && echo serverAuth || echo clientAuth)

openssl genrsa -out certs/"$NAME.key" 2048
openssl req -new -key certs/"$NAME.key" -subj "/CN=$NAME" -out certs/"$NAME.csr"
printf "basicConstraints=CA:FALSE\nextendedKeyUsage=%s\n" "$EKU" > certs/"$NAME.ext"
[ "$KIND" = "server" ] && echo "subjectAltName=DNS:localhost,IP:127.0.0.1" >> certs/"$NAME.ext"
openssl x509 -req -in certs/"$NAME.csr" -CA certs/ca.crt -CAkey certs/ca.key -CAcreateserial \
  -out certs/"$NAME.crt" -days 365 -sha256 -extfile certs/"$NAME.ext"
rm certs/"$NAME.csr" certs/"$NAME.ext"