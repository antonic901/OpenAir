package openair.service;

import openair.model.Mail;
import openair.service.interfaces.IMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class MailService implements IMailService {

    private final JavaMailSender javaMailSender;
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${custom.addr}")
    private String addr;

    @Value("${custom.fport}")
    private String port;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public boolean sendMail(Mail mail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage, true);

            mimeHelper.setSubject(mail.getMailSubject());
            mimeHelper.setFrom(mailFrom);
            mimeHelper.setTo(mail.getMailTo());
            mimeHelper.setText(mail.getMailContent());
            javaMailSender.send(mimeHelper.getMimeMessage());
            return true;
        } catch (Exception e) {
            logger.error("Exception while sending mail: {}", e.getMessage());
            return false;
        }
    }
}
