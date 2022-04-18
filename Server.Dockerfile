# syntax=docker/dockerfile:1
FROM openjdk:11.0.14.1-jdk-oraclelinux8
COPY . /p2pchat

WORKDIR p2pchat
RUN mkdir -p out/
RUN javac -d ./out/ ./src/br/com/pucrs/remote/api/*.java ./src/br/com/pucrs/client/*.java ./src/br/com/pucrs/server/*.java

COPY . .

WORKDIR out/
CMD ["java", "br.com.pucrs.server.Server"]


