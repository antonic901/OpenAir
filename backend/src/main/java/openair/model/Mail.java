package openair.model;

import lombok.*;
import openair.dto.MailDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Mail {
    public Mail(MailDTO mailDTO) {
		this.mailFrom = mailDTO.getMailFrom();
		this.mailTo = mailDTO.getMailTo();
		this.mailCc = mailDTO.getMailCc();
		this.mailBcc = mailDTO.getMailBcc();
		this.mailSubject = mailDTO.getMailSubject();
		this.mailContent = mailDTO.getMailContent();
		this.contentType = mailDTO.getContentType();
		this.attachments = mailDTO.getAttachments();
	}
	private String mailFrom;
    private String mailTo;
    private String mailCc;
    private String mailBcc;
    private String mailSubject;
    private String mailContent;
    private String contentType;
    private List <Object> attachments;
}
