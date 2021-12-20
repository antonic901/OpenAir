package openair.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Mail {

	@Email(message = "Email should be valid")
    @NotNull(message = "Sender cannot be null")
	private String mailFrom;

	@Email(message = "Email should be valid")
    @NotNull(message = "Recipient cannot be null")
    private String mailTo;

	@Email(message = "Email should be valid")
    private String mailCc;

	@Email(message = "Email should be valid")
    private String mailBcc;

	//subject and content has no size limit
    private String mailSubject;
    private String mailContent;

    private String contentType;
    private List <Object> attachments;
}
