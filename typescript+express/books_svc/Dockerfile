FROM node:8-slim

WORKDIR /server
COPY package*.json ./
RUN npm install

EXPOSE 8080

COPY . .

RUN npm run build
CMD npm run serve