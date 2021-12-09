package openair.email.service;

import openair.email.model.Mail;

public interface IMailService {
    boolean sendMail(Mail mail);
}
