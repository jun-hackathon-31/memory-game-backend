FROM gradle:7.4.2-jdk17

WORKDIR /app

COPY ./ .

RUN gradle stage

CMD ./build/install/app/bin/app

EXPOSE 8080