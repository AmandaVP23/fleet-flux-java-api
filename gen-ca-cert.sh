#!/bin/bash

openssl genrsa -out certs/ca.key 4096
openssl req -x509 -new -key certs/ca.key -sha256 -days 3650 \
  -subj "/CN=Fleet Dev CA" -addext "basicConstraints=critical,CA:TRUE" \
    -addext "keyUsage=critical,keyCertSign,cRLSign" \
    -addext "subjectKeyIdentifier=hash" -out certs/ca.crt

#CA_CERT_PASSWORD="${CA_CERT_PASSWORD:password}"

#openssl genrsa -out ca.key.pem -aes256 -passout pass:CA_CERT_PASSWORD

#openssl genrsa -out server.key 2048
#openssl req -new -key server.key -subj "/CN=localhost" -out server.csr
#
#cat > server.ext <<EOF
#basicConstraints = CA:FALSE
#extendedKeyUsage = serverAuth
#subjectAltName = DNS:localhost, IP:127.0.0.1
#EOF
#
#openssl x509 -req -in server.csr -CA ca.crt -CAkey ca.key -CAcreateserial \
#  -out server.crt -days 365 -sha256 -extfile server.ext
#
#openssl genrsa -out simulator.key 2048
#openssl req -new -key simulator.key -subj "/CN=simulator" -out simulator.csr
#
#cat > client.ext <<EOF
#basicConstraints = CA:FALSE
#extendedKeyUsage = clientAuth
#EOF
#
#openssl x509 -req -in simulator.csr -CA ca.crt -CAkey ca.key -CAcreateserial \
#c  -out simulator.crt -days 365 -sha256 -extfile client.ext
