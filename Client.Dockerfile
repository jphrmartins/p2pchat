# syntax=docker/dockerfile:1
FROM openjdk:12-ea-13-jdk-oraclelinux7
COPY . .
CMD ./build
COPY . .
CMD ./runClient $USERNAME $LOCALIP $SERVERIP $SERVERPORT $DIRECTORY