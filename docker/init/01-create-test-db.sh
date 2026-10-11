#!/bin/bash
set -e

psql
--username "$POSTGRES_USER"
--dbname "$POSTGRES_DB"
--set ON_ERROR_STOP=1
--command "CREATE DATABASE "fleet-flux-test" OWNER "$POSTGRES_USER";"