FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD master.zip /example/
WORKDIR /example/
RUN unzip master.zip
WORKDIR /example/tinystruct2.0-master
CMD ["./bin/dispatcher","--import-applications=org.tinystruct.system.TomcatServer", "--server-port=777", "--start-server"]
EXPOSE 777
