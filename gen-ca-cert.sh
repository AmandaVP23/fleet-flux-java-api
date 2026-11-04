#!/bin/bash

openssl genrsa -out certs/ca.key 4096
openssl req -x509 -new -key certs/ca.key -sha256 -days 3650 \
  -subj "/CN=Fleet Dev CA" -addext "basicConstraints=critical,CA:TRUE" \
    -addext "keyUsage=critical,keyCertSign,cRLSign" \
    -addext "subjectKeyIdentifier=hash" -out certs/ca.crt
