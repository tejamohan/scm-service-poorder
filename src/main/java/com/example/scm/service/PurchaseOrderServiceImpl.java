package com.example.scm.service;

import java.util.Random;

import javax.mail.MessagingException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.scm.beans.PurchaseOrderBo;
import com.example.scm.beans.PurchaseOrderDao;
import com.example.scm.beans.UserLogin;
import com.example.scm.repo.PurchaseOrderDaoData;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.mail.internet.MimeMessage;


@Service
public class PurchaseOrderServiceImpl implements PurchseOrderService{
	
    @Autowired
	private RestTemplate jt;
	
    @Autowired
    private JavaMailSender javaMailSender;
	
    @Autowired
	public PurchaseOrderDaoData dao;
	
	@Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;
    
     
    
    private String otp;
    
    


	@Override
	public boolean validateCredentials(String username, String password, String phoneNumber) throws Exception {
		boolean sendOtp=false;
		boolean verify=dao.validateCredentials(username, password);
		if(verify) {
			 sendOtp=sendOTP(username, phoneNumber);
		}else {
			throw new Exception("Invalid credentials");
		}
		return sendOtp;
	}
    
    
    public boolean sendOTP(String username, String phoneNumber) {
    	
    	
         otp = generateOtp();
        // Send OTP via Twilio API
        boolean otpSent = sendOTPViaTwilio(phoneNumber, otp);
        if (otpSent) {
          otpSent= dao.updateOTP(otp, username);
    
        }
        return otpSent;
    }
    
   
    public boolean sendOTPViaTwilio(String phoneNumber, String otp) {
    	
    	
        Twilio.init(accountSid, authToken);
        Message message = Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                "Your OTP is: " + otp)
                .create();
        System.out.println("OTP sent successfully. SID: " + message.getSid());
		return true;
    }
    
   
    
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
        return String.valueOf(otp);
    }

   

    
	
//	@Value("${pdf.api.url}") 
//    private String pdfApiUrl;
	

	
	

	@Override
	public String updatePoData(PurchaseOrderBo bo) {
		
           
		// TODO Auto-generated method stub
		return null;
	}


	
	@Override
	public String savePoData(PurchaseOrderBo bo) {
      PurchaseOrderDao poDao=new PurchaseOrderDao();
		
		BeanUtils.copyProperties(bo, poDao);
		
		Long id=dao.savePo(poDao);
		
		String apiUrl = "http://localhost:8181/pdf/generate?poid="+id;
		//String filename="report.xlsx";
		
//		  String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8181/pdf/generateExcel")
//	                .queryParam("poid", id)
//	                .queryParam("filename", filename)
//	                .toUriString();

	        // Make the HTTP request
//	        jt.postForObject(url, null, Void.class);
		
		 ResponseEntity<byte[]> responseEntity = jt.getForEntity(apiUrl, byte[].class);
		 
		 byte[] pdfBytes = responseEntity.getBody();

		  if (responseEntity.getStatusCode().is2xxSuccessful()) {
	            // Get the PDF content
	             pdfBytes = responseEntity.getBody();

	            // Send the PDF as an attachment via email
	            try {
					sendEmailWithAttachment("dileep.battini@gmail.com", "PDF Document", "Please find the attached PDF.", pdfBytes, "invoice.pdf");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
	        } else {
	            // Handle error response
	            // You might want to throw an exception or log an error
	            System.err.println("Error fetching PDF from the other application");
	        }
		
		//fetchDataFromApi(id);
		
		return id!=0?"data saved":"data not saved";
	}
	
	private void sendEmailWithAttachment(String toEmail, String subject, String text, byte[] attachment, String attachmentName) throws MessagingException, jakarta.mail.MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);

        
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addAttachment(attachmentName, new ByteArrayResource(attachment));
        javaMailSender.send(message);

//        javaMailSender.send(mimeMessage -> {
//            // Use MimeMessageHelper to add an attachment
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//            helper.setTo(toEmail);
//            helper.setSubject(subject);
//            helper.setText(text);
//
//            // Attach the PDF
//            helper.addAttachment(attachmentName, new ByteArrayResource(attachment));
//        });


	
	 
	
	

}


	@Override
	public boolean validateCredentials(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String saveCredentials(UserLogin user) {
	
		String response=dao.insertUser(user);
		return response;
	}




	
}
