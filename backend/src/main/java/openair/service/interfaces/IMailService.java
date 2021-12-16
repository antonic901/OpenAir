package openair.service.interfaces;

import openair.model.Mail;

public interface IMailService {
    boolean sendMail(Mail mail);
}
