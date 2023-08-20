package lab7.application.controller;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lab7.application.model.MailInfo;
import lab7.application.service.MailService;

@Controller
@RequestMapping("mail")
public class MailController {
	@Autowired
	MailService mailService;
	@Autowired
	ServletContext app;
	
	@RequestMapping(value = "sendMail",method = RequestMethod.GET)
	public String homeSend() {
		
		return"mail/mail";
	}
	
	@RequestMapping(value = "send",method = RequestMethod.GET)
	public String sendIndex(@ModelAttribute("mail") MailInfo mail) {
		return "mail/simple-send";
	}
	
	@ResponseBody
	@RequestMapping(value = "send",method = RequestMethod.POST)
	public String send(@ModelAttribute("mail") MailInfo mail) {
		try {
			mailService.send(mail.getTo(), mail.getSubject(), mail.getBody());
			return "OK";
		} catch (Exception e) {
			return "Lỗi send mail" + e.getMessage();
		}
		
	}
	
	@RequestMapping(value = "send-mutiple-mail",method = RequestMethod.GET)
	public String sendMutipleMail(@ModelAttribute("mail") MailInfo mail,@RequestParam("NumberTo") Optional<Integer> num
			,Model model) {
		if(!num.isPresent()) {
			model.addAttribute("NumberTo", 0);
		}else {
			model.addAttribute("NumberTo", num.get());
		}
		
		return "mail/send-mutiple-mail";
	}
	
	@RequestMapping(value = "store-mail",method = RequestMethod.POST)
	public String storeMailList(@ModelAttribute("mail") MailInfo mail, 
			RedirectAttributes model) throws MessagingException {
		mailService.queue(mail);
		mailService.getWaitingMail();
		model.addAttribute("NumberTo", mailService.getListMail().size());
		return "redirect:/mail/send-mutiple-mail";
	}
	
	@ResponseBody
	@RequestMapping("send-mutiple-mail")
	public String send5s() {
		try {
			mailService.getWaitingMail();
			mailService.run();
			return "Mail Bạn sẽ send trong giây lất";
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@RequestMapping(value ="send-with-attachment",method = RequestMethod.GET)
	public String loadFormAttachMent(@ModelAttribute("mail") MailInfo mail) {
		
		return "mail/send-attachment";
	}
	
	@ResponseBody
	@RequestMapping(value = "send-attachment", method = RequestMethod.POST)
	public String sendWithAttachment(@ModelAttribute("mail") MailInfo mail) {
		send(mail);
		return"OK";
	}
}
