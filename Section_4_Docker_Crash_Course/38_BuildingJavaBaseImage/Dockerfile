FROM ubuntu

WORKDIR java

ADD https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-x64_bin.tar.gz java17.tar.gz

RUN tar -xvzf java17.tar.gz \
    && rm java17.tar.gz

ENV PATH $PATH:/java/jdk-17.0.2/bin