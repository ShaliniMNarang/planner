package com.easyupload.photos.entity.Polly;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.polly.AmazonPollyClient;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.VoiceId;
import com.amazonaws.services.s3.AmazonS3;
import com.easyupload.photos.s3.S3Services;
import com.easyupload.photos.s3.S3ServicesImpl;
import com.easyupload.photos.util.Utility;

@Service
public class PollyServiceImpl implements PollyService{
	
	private Logger logger = LoggerFactory.getLogger(PollyServiceImpl.class);
	
	@Autowired
	private AmazonPollyClient client;
	
	@Autowired
	private S3Services s3Services;
	
	public String generateVoiceInvite(String eventId, String userName, String date, String time, String eventDesc) {
		String outputFileName = Utility.geyKeyName(eventId, "voiceinvite.mp4");    

		StringBuffer sb = new StringBuffer();
		sb.append("This message is from ");
		sb.append(userName);
		sb.append(". ");
		sb.append(eventDesc);
		sb.append(" on ");
		sb.append(date);
		sb.append(" at ");
		sb.append(time);


		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		try {
			SynthesizeSpeechRequest synthesizeSpeechRequest = new SynthesizeSpeechRequest()
					.withOutputFormat(OutputFormat.Mp3)
					.withVoiceId(VoiceId.Joanna)
					.withText(sb.toString());

			SynthesizeSpeechResult synthesizeSpeechResult = client.synthesizeSpeech(synthesizeSpeechRequest);

			s3Services.uploadFile(outputFileName,synthesizeSpeechResult.getAudioStream());			

		} catch (Exception ase) {
			logger.info("Caught an Exception in Polly:");
			logger.info("Error Message:    " + ase.getMessage());
		}
		
		return outputFileName;

	}

}
