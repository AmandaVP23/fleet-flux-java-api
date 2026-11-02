#!/usr/bin/env bash
# usage: ./gen-leaf.sh <name> <client|server>
# ./gen-leaf.sh device-123 client
set -euo pipefail
NAME=$1; KIND=$2
EKU=$([ "$KIND" = "server" ] && echo serverAuth || echo clientAuth)

openssl genrsa -out "$NAME.key" 2048
openssl req -new -key "$NAME.key" -subj "/CN=$NAME" -out "$NAME.csr"
printf "basicConstraints=CA:FALSE\nextendedKeyUsage=%s\n" "$EKU" > "$NAME.ext"
[ "$KIND" = "server" ] && echo "subjectAltName=DNS:localhost,IP:127.0.0.1" >> "$NAME.ext"
openssl x509 -req -in "$NAME.csr" -CA ca.crt -CAkey ca.key -CAcreateserial \
  -out "$NAME.crt" -days 365 -sha256 -extfile "$NAME.ext"
rm "$NAME.csr" "$NAME.ext"