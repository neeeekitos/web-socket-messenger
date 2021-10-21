# Web Socket Messenger : INSA project

There are 2 modules :
* SpringBoot App : used to run client & server servers
* React App : used as a web frontend and is connected to the client SpringBoot server

### Launch this project :
```
git clone git@github.com:neeeekitos/web-socket-messenger.git
// Import this project as Maven project in your IDE
// Launch ServerApplication  (web-socket-messenger/backend/src/main/java/server/)

// Before to launch ClientApplication, we have to change a port in web-socket-messenger/backend/src/main/resources/application.properties to 8081
// Launch ClientApplication (web-socket-messenger/backend/src/main/java/client/)

// Next we will launch React web app 
cd web-socket-messenger/frontend/src
yarn // install dependencies
yarn start // start the app on port 3001
```
