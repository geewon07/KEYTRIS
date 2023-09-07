FROM node:18.17.1-alpine3.17 as build

WORKDIR /app

# copy
COPY package*.json ./

RUN npm install

RUN ./build
COPY ./build ./build

# Prod Envrionment
FROM nginx:1.21.5-alpine

# 빌드 결과물 copy
COPY --from=build /app/build /usr/share/nginx/html

# 기본 Nginx 설정 삭제
RUN rm /etc/nginx/conf.d/default.conf

# custom 설정 copy
COPY /configure/nginx/nginx.conf /etc/nginx/conf.d/

# 포트 개방
EXPOSE 80

# Nginx 서버 실행 
CMD ["nginx", "-g", "damon off;"]