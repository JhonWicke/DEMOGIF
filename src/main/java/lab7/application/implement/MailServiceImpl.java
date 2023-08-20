package lab7.application.implement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lab7.application.model.MailInfo;
import lab7.application.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	JavaMailSender sender;

	@Value("${spring.mail.username}")
	String from;

	@Override
	public void send(String to, String subject, String body) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body, true);
		helper.setReplyTo(from);
		sender.send(message);
	}

	@Override
	public void send(MailInfo mail) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
		helper.setFrom(mail.getFrom());
		helper.setTo(mail.getTo());
		helper.setSubject(mail.getSubject());
		helper.setText(mail.getBody(), true);
		helper.setReplyTo(mail.getFrom());
		String[] cc = mail.getCc();
		if (cc != null && cc.length > 0) {
			helper.setCc(cc);
		}
		String[] bcc = mail.getBcc();
		if (bcc != null && bcc.length > 0) {
			helper.setBcc(bcc);
		}
		String[] attachments = mail.getAttachments();
		if (attachments != null && attachments.length > 0) {
			for (String path : attachments) {
				File file = new File(path);
				helper.addAttachment(file.getName(), file);
			}
		}
		sender.send(message);
	}

	List<MimeMessage> mails = new ArrayList<>();

	@Override
	public void queue(MailInfo mail) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
		helper.setFrom(from);
		helper.setTo(mail.getTo());
		helper.setSubject(mail.getSubject());
		helper.setText(mail.getBody(), true);
		helper.setReplyTo(from);
		String[] cc = mail.getCc();
		if (cc != null && cc.length > 0) {
			helper.setCc(cc);
		}
		String[] bcc = mail.getBcc();
		if (bcc != null && bcc.length > 0) {
			helper.setBcc(bcc);
		}
		String[] attachments = mail.getAttachments();
		if (attachments != null && attachments.length > 0) {
			for (String path : attachments) {
				File file = new File(path);
				helper.addAttachment(file.getName(), file);
			}
		}
		mails.add(message);
		
	}

	@Override
	public void queue(String to, String subject, String body) throws MessagingException {
		MailInfo mail = new MailInfo(to, subject, body);
		this.queue(mail);
	}

	@Override
	@Scheduled(fixedDelay = 5000)
	public void run() {
		int sucess = 0, error = 0;
		while (!mails.isEmpty()) {
			MimeMessage message = mails.remove(0);
			try {
				sender.send(message);
				sucess++;
			} catch (Exception e) {
				error++;
			}
		}
		System.out.printf(">> Sent :%d, Error: %d\r,\n",sucess,error);
	}

	@Override
	public void getWaitingMail() {
		if(mails.isEmpty()) {
			System.out.println("No message");
		}else {
			
			for (MimeMessage mimeMessage : mails) {
				MimeMessageParser parser = new MimeMessageParser(mimeMessage);
				try {
					parser.parse();
					System.out.println(parser.getTo());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<MimeMessage> getListMail() {
		return mails;
	}

}
