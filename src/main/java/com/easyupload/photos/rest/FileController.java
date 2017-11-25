package com.easyupload.photos.rest;

	
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;
import javax.websocket.server.PathParam;
import javax.ws.rs.FormParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.easyupload.photos.s3.DocumentAPIItemCRUDExample;
import com.easyupload.photos.s3.S3Services;
import com.easyupload.photos.util.EmailService;
import com.easyupload.photos.util.MMSService;
import com.easyupload.photos.util.Utility;
import com.easyupload.photos.entity.Comment;
import com.easyupload.photos.entity.Event;
import com.easyupload.photos.entity.EventPhoto;
import com.easyupload.photos.entity.EventPhotoRepository;
import com.easyupload.photos.entity.EventRepository;
import com.easyupload.photos.entity.EventUsers;
import com.easyupload.photos.entity.EventUsersRepository;
import com.easyupload.photos.entity.FileUpload;
import com.easyupload.photos.entity.FileUploadRepository;
import com.easyupload.photos.entity.Invitee;
import com.easyupload.photos.entity.InviteeRepository;
import com.easyupload.photos.entity.Polly.PollyService;
import com.easyupload.photos.model.FileDetail;

@RestController
@RequestMapping("/event-planner")
//@RequestMapping("/event-planner/event-planner/")
@MultipartConfig(fileSizeThreshold = 20971520)
public class FileController {

	@Autowired
	private S3Services s3Services;
	
	@Autowired
	private FileUploadRepository fileUploadRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private EventPhotoRepository eventPhotoRepository;
	
	@Autowired
	private InviteeRepository inviteeRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private MMSService mmsService;
	
	@Autowired
	private  EventUsersRepository eventUsersRepository;
	
	@Autowired
	private PollyService pollyService;
	
	@Value("${s3.bucket}")
	private String s3Bucket;
	
	@RequestMapping("/hi")
	 public String sayHi() {
		 return "Hi";
	 }
	
	@RequestMapping(value = "/getfiles")
    public List<EventPhoto> getFileNames(Long eventId) {
		//return s3Services.getFileNames();
		//SN : Updated, will fetch file details from the DB
		return eventPhotoRepository.findByEventId(eventId);
		
	}
	
	@RequestMapping(value = "/getfiles/{fileName}")
    public FileDetail getFileName(@PathVariable("fileName") String fileName) {
		System.out.println("getFileNames:"+fileName);
		List<FileUpload> fileUpload=fileUploadRepository.findByFileName(fileName+".txt");
		FileDetail fileDetail=null;
		if (fileUpload.size() >0) {
			System.out.println("found");
			fileDetail=new FileDetail(fileName,fileUpload.get(0).getFileDesc(),
									fileUpload.get(0).getFirstName()+" "+fileUpload.get(0).getLastName(),
									fileUpload.get(0).getUpdateDate());
		}
		System.out.println("fileDetail:"+fileDetail.getFileDesc());
		return fileDetail;
	}
	
	@RequestMapping(value = "/upload")
    public String uploadFile(
    			@RequestParam(value="firstName", required=true) String firstName,
            @RequestParam(value="lastName", required=true) String lastName,
            @RequestParam("uploadedFile") MultipartFile uploadedFileRef,
    			@RequestParam(value="fileDesc") String fileDesc){
		FileInputStream uploadedInputStream=null;
		try {
			String fileName=uploadedFileRef.getOriginalFilename();
			fileUploadRepository.save(new FileUpload(firstName,lastName,fileName,fileDesc,Utility.dateFormatter(new Date()),Utility.dateFormatter(new Date())));
			uploadedInputStream =  (FileInputStream) uploadedFileRef.getInputStream();
			s3Services.uploadFile(fileName,uploadedInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "upload failed!!";
		}
		return "successfully upload";
	}
	
	@RequestMapping(value = "/delete")
    public String deleteFile(@RequestParam("fileName")  String fileName) {
		System.out.println("deleteFile:"+fileName);
		try {
			s3Services.deleteFile(fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return "delete failed!!";
		}
		return "successfully deleted";
	}
	
	@RequestMapping(value = "/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("fileName")  String fileName) {
		System.out.println("downloadFile:"+fileName);
		InputStreamResource resource=null;
		try {
			resource=s3Services.downloadFile(fileName,"CF");
			//resource=s3Services.downloadFile(fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION,
		                  "attachment;filename=" + fileName)
		            .contentType(MediaType.TEXT_PLAIN).contentLength(4)
		            .body(resource);
	}
	
	//used
	@RequestMapping(value = "/photos/{eventid}")
	public String getPhotosHTML(@PathVariable("eventid") Long eventId) {
		String htmlBody="";
		//String imgURL="http://d30lkyn29uha7q.cloudfront.net/";
		String imgURL="https://s3-us-west-2.amazonaws.com/" + s3Bucket + "/";
		List<EventPhoto> files=getFileNames(eventId);
		//String[] imgArr= {"20131002_082818.jpg","20131001_192937.jpg","20131002_082818.jpg","20131001_192937.jpg","20131002_082818.jpg","20131001_192937.jpg"};
		for(EventPhoto ep:files) {
			htmlBody = htmlBody+"<span><img class=\"img-thumbnail\" alt=\"Cinque Terre\" width=\"304\" height=\"236\" src=\""+imgURL+ep.getS3Url()+"\"/></span>";
		}

		String eventName=eventRepository.findByEventId(eventId).get(0).getTitle();

		String htmlHeader="<!DOCTYPE html>\n" + 
				"<html lang=\"en\">\n" + 
				"<head>\n" + 
				"  <title>"+eventName+"</title>\n" + 
				"  <meta charset=\"utf-8\">\n" + 
				"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" + 
				"  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n" + 
				"  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n" + 
				"  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"\n" + 
				"<div class=\"container\">"+
				"<h2>"+eventName+"</h2>";
		String htmlFooter="</div>\n" + 
				"\n" + 
				"</body>\n" + 
				"</html>";
		List<Comment> comments=s3Services.getItems(eventId);
		String strStart="<br/><br/><h1>Comments</h1><br/><div class=\"list-group\">";
		String strBody="";
		for (Comment c:comments) {
			strBody=strBody+"<div class=\"list-group-item list-group-item-action flex-column align-items-start\"><p class=\"mb-1\">"+c.getCommentDesc()+"</p></div>";
		}
		String strEnd="</div>";
		String addCommentHtml="<br/><br/><form action=\"/event-planner/event-planner/comments\" method=\"POST\">" +
				"<div class=\"form-group\">\n" + 
				"    <label for=\"comment\">Add Comment</label>"+
				"<textarea class=\"form-control\" id=\"comment\" name=\"comment\" rows=\"3\"></textarea>\n" + 
				"</div>\n" + 
				"<div>\n" + 
				"	<button type=\"submit\" onclick=\"submitformClick()\" class=\"btn btn-primary\">Submit</button>\n" +
				"<textarea type=\"text\" class=\"form-control invisible\" id=\"eventId\" name=\"eventId\" rows=\"3\">"+eventId+"</textarea>\n" +
				"</div>\n" + 
				"</form>"+
				"<script type=\"text/javascript\">\n" + 
				"function submitformClick()\n" + 
				"{\n" + 
				"console.log('button clicked');\n"+
				"setTimeout(function(){ window.history.back(); }, 3000);"+
				"}\n" + 
				"</script>\n" + 
				"";
		String commentsHtml=strStart+strBody+strEnd;
		String html=htmlHeader+htmlBody+commentsHtml+addCommentHtml+htmlFooter;

		return html;
		
	}
	
	@RequestMapping("/comments")
	public String addComment(@FormParam("eventid") Long eventId,
			@FormParam("comment") String comment) {
		System.out.println("addComment");
		System.out.println(eventId);
		System.out.println(comment);
		return s3Services.createItems(eventId,comment);
	}
	
	@RequestMapping("/comment/{eventid}")
	public List<Comment> getComment(@PathVariable("eventid") String eventId) {
		System.out.println("getComment");
		return s3Services.getItems(Long.parseLong(eventId));
	}	
	
	//used
	@RequestMapping("/createevent")
	public Event createEvent(@RequestParam(value="title", required=true) String title,
			@RequestParam(value="location", required=true) String location,
			@RequestParam(value="eventDate", required=true) String eventDate,
			@RequestParam(value="eventTime", required=true) String eventTime,
			@RequestParam(value="description") String description,
			@RequestParam(value="userId") String userId) {
		
		Event event=new Event(userId,title,location,eventDate,eventTime,description);
		event = eventRepository.save(event);
		
		String voiceKey = pollyService.generateVoiceInvite(Long.toString(event.getEventId()), userId, eventDate, eventTime, description);
				
		event.setVoiceInviteKey(voiceKey);
		return eventRepository.save(event);
	}
	
	//used
	@RequestMapping("/getevents")
	public List<Event> createEvent() {
		return eventRepository.findAll();
	}
	
	//used
	@RequestMapping("/getevents/{eventId}")
	public Event getEvent(@PathVariable("eventId") Long eventId) {
		return eventRepository.findByEventId(eventId).get(0);
	}	
	
	//used
	@RequestMapping("/getinvitees/{eventId}")
	public List<Invitee> getInvitees(@PathVariable("eventId") Long eventId) {
		return inviteeRepository.findByEventId(eventId);
	}
	
	//used
	@RequestMapping("/sendemail")
	public String sendEmail(@RequestParam(value="eventid", required=true) String eventId,
			@RequestParam(value="id", required=true) String id,
			@RequestParam(value="name", required=true) String name,
			@RequestParam(value="email", required=true) String email,
			@RequestParam(value="phone", required=true) String phone,
			@RequestParam(value="type", required=true) String type) {
		
		//snarang : commented as we get charged for every mms that gets sent
		mmsService.sendMMS(eventId);
		inviteeRepository.save(new Invitee(Long.parseLong(id),Long.parseLong(eventId),email,phone,name));
		List<String> emails=new ArrayList<>();
		emails.add(email);
		return emailService.sendEmail(type, name, emails,Long.parseLong(eventId));
	}
	
	//used
	@RequestMapping(value = "/uploadphoto")
    public String uploadPhoto(
    			@RequestParam(value="eventid", required=true) Long eventId,
            @RequestParam("photo") MultipartFile uploadedFileRef){
		System.out.println("uploadPhoto");
		FileInputStream uploadedInputStream=null;
		try {
			String fileName=uploadedFileRef.getOriginalFilename();
			String keyName = Utility.geyKeyName(Long.toString(eventId), fileName);
			eventPhotoRepository.save(new EventPhoto(eventId,fileName, keyName,null));
			uploadedInputStream =  (FileInputStream) uploadedFileRef.getInputStream();
			s3Services.uploadFile(keyName,uploadedInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "photo upload failed!!";
		}
		return "photo successfully upload";
	}
	
	//used
	@RequestMapping("/signUp")
	public String signUp(@RequestParam(value="userId", required=true) String userId,
			@RequestParam(value="firstName", required=true) String firstName,
			@RequestParam(value="lastName", required=true) String lastName,
			@RequestParam(value="password", required=true) String password,
			@RequestParam(value="mailId", required=true) String mailId) {
		
		eventUsersRepository.save(new EventUsers(userId, firstName, lastName, password, mailId));
		
		return "SUCCESS";
	}
	
	//used
	@RequestMapping("/login")
	public String login(@RequestParam(value="userId", required=true) String userId,
			@RequestParam(value="password", required=true) String password) {		
		EventUsers user = eventUsersRepository.findByUserId(userId);
		if(user!=null && user.getPassword().equals(password)) {
			return "SUCCESS";
		}else {
			return "FAIL";
		}
	}
}
