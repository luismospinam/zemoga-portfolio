#zemoga-portfolio

The application was written in Java 11 using Maven and spring boot 2, which can be accessed in the url: http://localhost:8080/zemoga_portfolio_api/

This is a Dockerize project which can be simply run by using Docker run/build commands. Once in the root folder of the app run the following commands.

* docker build -t zemoga-portfolio .
* docker run -p 8080:8080 zemoga-portfolio

It can also be compiled as a WAR file which can be deployed in a tomcat server with the following command (the result will be in /target folder of the app):

* mvn clean install
	
**Note:** Building the app will require all the test to pass, unit tests and integration API tests)



Once the application is up and running:

* The documentation of the API can be accessed in the following link: http://localhost:8080/zemoga_portfolio_api/swagger-ui.html/
* It exposes a simple web UI which can be accessed in the following Link: http://localhost:8080/zemoga_portfolio_api/
* It exposes two endpoints in the Back-End
	* GET /user_info/{idPortfolio} -> Give the information of the user and integrate with Twitter API to get the last 5 twitters from that user (configurable).
	* POST /modify_user_info/{idPortfolio} ->  Modify the fiven "idPortfolio" with the body sent to this request.
	
	
Time spent writing this application: 10 Hours.