# syntax=docker/dockerfile:1
FROM openjdk:12-ea-13-jdk-oraclelinux7
COPY . .
CMD build.bat
COPY . .
CMD runClient.bat $USERNAME $LOCALIP $SERVERIP $SERVERPORT $DIRECTORY