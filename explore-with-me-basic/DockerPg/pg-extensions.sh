#!/bin/sh
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
create extension if not exists cube;
create extension if not exists earthdistance;
select * FROM pg_extension;
EOSQL