# syntax=docker/dockerfile:1
FROM rtfpessoa/ubuntu-jdk8:2.0.40
COPY . .
CMD ./build
CMD ls
COPY . .
CMD ./runServer
