# syntax=docker/dockerfile:1
FROM rtfpessoa/ubuntu-jdk8:2.0.40
COPY . .
CMD build.bat
COPY . .
CMD runServer.bat
