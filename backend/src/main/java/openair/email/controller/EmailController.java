package openair.email.controller;

import openair.email.dto.MailDTO;
import openair.email.model.Mail;
import openair.email.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/email")
public class EmailController {
	
	private MailService emailService;
	
	@Autowired
	public EmailController(MailService emailService) {
		this.emailService = emailService;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/send-mail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean sendMessagesToAllSubscribers(@RequestBody MailDTO emailDTO) throws Exception{
		Mail mail = new Mail(emailDTO.getMailFrom(),emailDTO.getMailTo(),emailDTO.getMailCc(),emailDTO.getMailBcc(),
				emailDTO.getMailSubject(),emailDTO.getMailContent(),emailDTO.getContentType(),emailDTO.getAttachments());
		return emailService.sendMail(mail);
	}
	
}