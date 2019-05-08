FROM nginx:latest

RUN rm -rf /usr/share/nginx/html/*

COPY ./nginx.conf /etc/nginx/nginx.conf
COPY ./dist/scala-play-angular-seed /var/www/html

EXPOSE 80
