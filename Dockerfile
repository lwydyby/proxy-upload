FROM openjdk:8-jre-alpine

WORKDIR /
RUN apk update \
    && apk add curl \
    && apk add tzdata

ADD build/libs/loops-all.jar loops.jar
ENV TZ Asia/Shanghai


EXPOSE 9000


ENV JAVA_OPTS="\
-XX:+UnlockExperimentalVMOptions\
-XX:+UseCGroupMemoryLimitForHeap"


CMD java ${JAVA_OPTS} -jar loops.jar

