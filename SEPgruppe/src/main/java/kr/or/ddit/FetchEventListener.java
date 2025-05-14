package kr.or.ddit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import kr.or.ddit.works.mail.service.MailService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FetchEventListener {
	
	@Autowired
    private MailService mailService;
	
	@Async
	@EventListener(classes = FetchEvent.class)
	public void eventListener(FetchEvent event) throws Exception {
		Authentication authentication = event.getAuthentication();
		
		mailService.getMailList(authentication.getName());

	}
}
