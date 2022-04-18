# syntax=docker/dockerfile:1
FROM openjdk:11.0.14.1-jdk-oraclelinux8
COPY . /p2pchat
VOLUME /resources
COPY /resources /resources
WORKDIR p2pchat

RUN mkdir -p out/
RUN javac -d ./out/ ./src/br/com/pucrs/remote/api/*.java ./src/br/com/pucrs/client/*.java ./src/br/com/pucrs/server/*.java

ARG 1
ARG 2
ARG 3
ARG 4
ARG 5

ENV 1=${1}
ENV 2=${2}
ENV 3=${3}
ENV 4=${4}
ENV 5=${5}

COPY . .

WORKDIR out/

CMD ["java", "br.com.pucrs.client.ClientApp", "$1", "$2", "$3", "$4", "$5"]