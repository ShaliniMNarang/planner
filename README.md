## CPM281_Cloud Technologies Project 2 for Cohort 9 SJSU-FALL-2017 by Group 11 

### Class Details
- Fall 2017
- University Name: http://www.sjsu.edu/
- Course: Cloud Technologies (http://info.sjsu.edu/web-dbgen/catalog/courses/CMPE281.html)
- Professor Sanjay Garje (https://www.linkedin.com/in/sanjaygarje/)
- ISA: Divyankitha Urs (https://www.linkedin.com/in/divyankithaurs/)

### Team members
#### GROUP 11
- Sy Le (https://www.linkedin.com/in/syle1021/)
- Chidananda Pati (https://www.linkedin.com/in/chidananda-pati-87235356/)
- Shalini Narang
- Puja Kawale (https://www.linkedin.com/in/puja-kawale-126a1357)

### Introduction to "Event Wedding Planner"
This Application platforms a polished and straightforward catering focused on the creation and sending of electronic invitations. Users can create an event, fill in information fields pertaining to their wedding event, build a guest list, and finally send the invitation via e-mail out to individuals using their email contact list. It also allows the user to upload event photos and comment on them.
Website - http://eventplanner.themodestwhite.com

### Application UI 
![Screenshot](welcome.png)
![Screenshot](chatbot.png)
![Screenshot](BIReport.png)

### Prereuisite Setup
To be able to run this project locally below pre-requisites are required to be made -</br></br>
1)Register/create to an AWS account</br>
2)Create an Amazon S3 bucket with defined security policies</br>
3)Create and activate Amazon MySQL, RDS instance</br>
4)Create Dynamo DB</br>
5)Create an IAM previledged user, generate access tokens for this user</br>
6)Softwares to download locally before running this application</br>
  -java 8 SDK</br>
  -Eclipse oxygen with STS</br>
  
NOTE - Update application.properties file for below parameters -</br>
-aws.access_key_id</br>
-aws.secret_access_key</br>
-s3.bucket</br>
-s3.region</br>
-s3.uploadfile</br>
-s3.key</br>
-server.port </br>
-spring.datasource.url</br>
-spring.datasource.usernam </br>
-spring.datasource.password</br>
-email.userName</br>
-email.password</br>
-twilio.acount_id</br>
-twilio.auth_token</br>

### Steps to run the project locally</br>
-Download the source code from GIT and import it as a maven project into eclipse. When the project is imported the maven pom.xml will resolve and import all the dependencies. The dependencies include AWS SDK, Spring Boot.</br>
-Run Project in eclipse as spring boot App</br>
-Check the application on "http://localhost:"server.port"/event-planner/"</br>

