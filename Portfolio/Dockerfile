FROM node:8
WORKDIR /docker/
COPY . /docker/
ENV PATH /usr/src/app/node_modules/.bin:$PATH
RUN npm install
CMD npm start
EXPOSE 4200