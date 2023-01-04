### STAGE 1: Build ###
FROM node:16.10-alpine AS build
RUN npm install -g @angular/cli@14.2.6 @angular/core @angular/common
WORKDIR /usr/src/app
COPY package.json package-lock.json ./
RUN npm install --legacy-peer-deps
COPY . ./
RUN ng build
### STAGE 2: Run ###
FROM nginx:1.17.1-alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /usr/src/app/dist/view /usr/share/nginx/html
