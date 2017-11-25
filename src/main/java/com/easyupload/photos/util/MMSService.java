package com.easyupload.photos.util;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.easyupload.photos.entity.Event;
import com.easyupload.photos.entity.EventRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class MMSService {
	@Value("${twilio.acount_id}")
	private String accountId;

	@Value("${twilio.auth_token}")
	private String authToken;

	@Value("${s3.bucket}")
	private String s3Bucket;
	
	@Autowired
	private EventRepository eventRepository;
	
	public void sendMMS(String eventId){
		Event event = eventRepository.findByEventId(Long.parseLong(eventId)).get(0);
		String url = "https://s3-us-west-2.amazonaws.com/" +s3Bucket+"/" + event.getVoiceInviteKey();
		
		Twilio.init(accountId, authToken);
		
		PhoneNumber to = new PhoneNumber("+14252337467");
		PhoneNumber from = new PhoneNumber("+12062038385");
		try {
		
			Message message = Message
		            .creator(to, from,                     
		                   "Message from eventPlanner. Pl. click on following URL to listen to the message. " + url)
		            //.setMediaUrl(new URI(url))
		            .create();
			
			System.out.println(message.getSid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
