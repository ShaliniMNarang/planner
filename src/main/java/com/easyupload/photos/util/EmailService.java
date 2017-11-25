package com.easyupload.photos.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.easyupload.photos.entity.Event;
import com.easyupload.photos.entity.EventRepository;

@Service
public class EmailService {
	@Value("${email.userName}")
	private String fromEmail;

	@Value("${email.password}")
	private String password;

	@Autowired
	private EventRepository eventRepository;
	
	@Value("${s3.bucket}")
	private String s3Bucket;
	
	public String sendEmail(String type,String name,List<String> toEmails,Long eventId){
	System.out.println(fromEmail);
	System.out.println(password);
    try{
        final String toEmail = toEmails.get(0); // can be any email id 

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        String emailMessage=null;
        Event event=eventRepository.findByEventId(eventId).get(0);
        if (type.equals("invite")) {
        		emailMessage="Hi "+name+",\n\n"+
					"You are invited to attend the event:\n\n"+
        				event.getTitle()+", "+event.getEventDate()+", "+event.getEventTime()+"\n\n"+
					"Pl. click on following link to listen to the message from the host " + 
					"https://s3-us-west-2.amazonaws.com/" +s3Bucket+"/" + event.getVoiceInviteKey() + "\n\n" + 
					"Cheers,";
        } else {
        		emailMessage="Hi "+name+",\n\n"+
        				"Thank you for attending the event.\n"+
        				"Please find the photos of the event http://localhost:8080/event-planner/event-planner/photos/"+eventId+"\n\n"+
					"Cheers,";
        }
        
        String emailSubject=event.getTitle();
        message.setSubject(emailSubject);
        message.setText(emailMessage);

        Transport.send(message);
        return "Mail Sent";
    }catch(Exception ex){
        System.out.println(ex);
        return "Mail fail";
    }
}

}
