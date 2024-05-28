# Dockerfile for my postgresql database container
FROM postgres:latest

COPY init.sql /docker-entrypoint-initdb.d/

ENV POSTGRES_USER=lauti
ENV POSTGRES_PASSWORD=test123