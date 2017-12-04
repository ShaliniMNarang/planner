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

### Project - 

### Introduction to Application
Wedding is an important occasion. It becomes difficult to manage the occasion and arrangements from different channels. Nowadays we have wedding planning companies who take complete responsibility of event with hefty budget. But, our solution is designed to target the public who have access to internet, knowledge to use the online services and want to save cost with not much manpower around.</br>
Application name : Event Wedding Planner</br>
This Application platforms a polished and straightforward catering focused on the creation and sending of electronic invitations. Users can create an event, fill in information fields related to their wedding event, create a guest list, and conclusively send the invitation via e-mail out to guest using their email contact list. It also allows the user to upload event photos and comment on them.</br>
Website - http://eventplanner.themodestwhite.com


### Application UI 
![Screenshot](welcome.png)
![Screenshot](list.png)
![Screenshot](Uploadpicture.png)
![Screenshot](comment.png)
![Screenshot](guest.png)
![Screenshot](chatbot.png)
![Screenshot](BIReport.png)

### Prereuisite Setup
To be able to run this project locally below pre-requisites are required to be made -</br>
- Register/create to an AWS account</br>
- Create an Amazon S3 bucket with defined security policies</br>
- Create and activate Amazon MySQL, RDS instance</br>
- Create Dynamo DB</br>
- Create an IAM previledged user, generate access tokens for this user</br>
- Softwares to download locally before running this application</br>
  - java 8 SDK</br>
  - Eclipse oxygen with STS</br>
- Update application.properties file for below parameters -</br>
  - _aws.access_key_id</br>_
  - _aws.secret_access_key</br>_
  - _s3.bucket</br>_
  - _s3.region</br>_
  - _s3.uploadfile</br>_
  - _s3.key</br>_
  - _server.port </br>_
  - _spring.datasource.url</br>_
  - _spring.datasource.usernam </br>_
  - _spring.datasource.password</br>_
  - _email.userName</br>_
  - _email.password</br>_
  - _twilio.acount_id</br>_
  - _twilio.auth_token</br>_

### Steps to run the project locally</br>
- Download the source code from GIT and import it as a maven project into eclipse. When the project is imported the maven pom.xml will resolve and import all the dependencies. The dependencies include AWS SDK, Spring Boot.</br>
- Run Project in eclipse as spring boot App</br>
- Check the application on "http://localhost:"server.port"/event-planner/"</br>

