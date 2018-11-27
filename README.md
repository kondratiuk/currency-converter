# currency-converter
09-12-2015 

"On-Line High Precision Currency Converter" is an Open Source web application for getting currency rates on-line and converts them in each other. 
Precision and amount of selected currency can be adjusted quite flexible. 

To make war file for deployment, please run "mvn package" for the project in IDE or CLI and you'll find war file in /target folder. 
For application deployment the Tomcat is recommended. To login in the application use email/password as login/psw. Simple pre-registration is required on  welcome page of the application. 

Used technology stack:
Java EE, Spring MVC/Spring Security, JSF/JSTL 2.0, HTML, JSON, Jetty, Maven, Git, Tomcat, Eclipse 4.5.1, JavaMelody, etc.

Application is available on the next public servers:
Heroku: https://c-converter.herokuapp.com (web page loading might take some time (upto 1 min) initially since Heroku account is free)
Monitoring: https://c-converter.herokuapp.com/monitoring  
Travis-CI: https://travis-ci.org/kondratiuk/currency-converter 
