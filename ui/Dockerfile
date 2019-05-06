FROM nginx:1.14.1-alpine

COPY ./nginx.conf /etc/nginx/conf.d/

RUN rm -rf /usr/share/nginx/html/*

COPY ./dist/scala-play-angular-seed /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
