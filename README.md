################################About the game####################

The object of the game is to collect as many stones in your mancala as possible. 
Each player takes turn to move the stones.

################################Technology stack##################

* Java 1.8
* Spring Boot 2.x
* Swagger 3
* Thymeleaf
* Maven

##############################How to build#########################

mvn clean install 

The Web interface can be accessed from browser by hitting http://localhost:8080/

To test the backend API's - http://localhost:8080/swagger-ui/index.html

############################Configuration of the game##############

Game settings can be configured from settings.js file which can be found under resources/js folder. 
Following things can be configured:
 * Player's Names
 * Number of small Pits
 * Stones in small Pits
