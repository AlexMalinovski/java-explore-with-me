FROM postgres:14-alpine
COPY pg-extensions.sh /docker-entrypoint-initdb.d/
RUN chmod 755 /docker-entrypoint-initdb.d/pg-extensions.sh