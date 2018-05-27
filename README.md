   ___   ____      ____  _       _______     ________
 .'   `.|_  _|    |_  _|/ \     |_   __ \   |_   __  |
/  .-.  \ \ \  /\  / / / _ \      | |__) |    | |_ \_|
| |   | |  \ \/  \/ / / ___ \     |  __ /     |  _| _
\  `-'  /   \  /\  /_/ /   \ \_  _| |  \ \_  _| |__/ |
 `.___.'     \/  \/|____| |____||____| |___||________|


The rules of this game are presented in here: https://www.youtube.com/watch?v=v9LNUvHqXeQ&t=191s

The game is build on 2 layers:
  
    1) Front End based on a Angular 6 app using WebPack as a server
    
    2) Back End based on a Spring Boot app on jdk 8

To simply run the game you can use gradle wrapper ( or gradle directly, if your gradle version is 4.6 ) as following:

    ./gradlew start

To stop the front end server use:

    ./gradlew stop

Gradle should install all the dependencies before running the app, although some errors may occur with npm.
In case of that, delete the 'node_modules' folder from 'frontend/' and run command 'npm install'

The game be started without gradle as following:
  
  Front End
    - change directory to {projectdir}/frontend
    - run 'npm install' command
    - run 'npm start' command
    
  Back End
    - can be manually started from IDE ( you'll need to install Lombok plugin beforehand )
