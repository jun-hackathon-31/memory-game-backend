FROM openjdk:17-oracle

WORKDIR /app

COPY ./ .

RUN gradle stage

CMD ./build/install/app/bin/app

EXPOSE 8080