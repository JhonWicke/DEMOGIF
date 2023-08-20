package lab7.application.service;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.scheduling.annotation.Scheduled;

import lab7.application.model.MailInfo;

public interface MailService {
	void send(MailInfo mail) throws MessagingException;

	void send(String to, String subject, String body) throws MessagingException;

	void queue(MailInfo mail) throws MessagingException;

	void queue(String to, String subject, String body) throws MessagingException;

	void run();
	
	void getWaitingMail();
	
	List<MimeMessage> getListMail();
	
}
