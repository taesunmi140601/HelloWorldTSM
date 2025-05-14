package kr.or.ddit.works.mail.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;

import kr.or.ddit.works.mail.config.GMailConfig;
import kr.or.ddit.works.mail.exception.NeedOAuthRedirectException;
import kr.or.ddit.works.mail.vo.AttachmentDTO;
import kr.or.ddit.works.mail.vo.MailReceptionVO;
import kr.or.ddit.works.mail.vo.MailSentLogVO;
import kr.or.ddit.works.mail.vo.MailUserAuthVO;
import kr.or.ddit.works.mybatis.mappers.MailMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Autowired
    private MailMapper mailMapper;

    @Lazy
    @Autowired
    private GMailConfig mailConfig;

    @Override
    public void getMailList(String userId) throws Exception {
        Gmail service = mailConfig.getGmailService(userId);
        ListMessagesResponse response = service.users().messages()
            .list("me")
            .setMaxResults(50L)
            .execute();
        List<Message> messages = response.getMessages();

        // 수신 메일과 발신 메일을 각각 저장할 리스트 선언
        List<MailReceptionVO> receptionMailList = new ArrayList<>();
        List<MailSentLogVO> sentMailList = new ArrayList<>();

        if (messages != null) {
            for (Message message : messages) {
                // 메일 전체 정보를 가져오면서 라벨 정보도 포함됨
                Message fullMessage = service.users().messages().get("me", message.getId()).execute();
                List<String> labelIds = fullMessage.getLabelIds();

                // 헤더값들을 임시 변수에 저장 (두 VO에서 공통으로 사용할 값)
                String subject = "";
                String fromEmail = "";
                String toEmail = "";
                Timestamp mailDate = null;
                for (MessagePartHeader header : fullMessage.getPayload().getHeaders()) {
                    switch (header.getName()) {
                        case "Subject":
                            subject = header.getValue();
                            break;
                        case "From":
                            fromEmail = header.getValue();
                            break;
                        case "To":
                            toEmail = header.getValue();
                            break;
                        case "Date":
                            try {
                                String dateStr = header.getValue(); // 예: "Wed, 09 Apr 2025 07:17:23 GMT"
                                dateStr = dateStr.replaceAll("\\s*\\(.*\\)$", "").trim();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                                ZonedDateTime zdt = ZonedDateTime.parse(dateStr, formatter);
                                // 원하는 시간대로 변환 (예: Asia/Seoul)
                                LocalDateTime dateTimeWithoutNano = zdt.withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                                                                         .toLocalDateTime()
                                                                         .withNano(0);
                                mailDate = Timestamp.valueOf(dateTimeWithoutNano);
                            } catch (DateTimeParseException e) {
                                log.warn("날짜 파싱 실패: {}", header.getValue(), e);
                            }
                            break;
                    }
                }
                
                // 라벨 분기 처리
                if (labelIds != null) {
                    if (labelIds.contains("SENT")) {
                        // SENT가 포함된 경우 - 보낸 메일로 처리 (보낸 메일 우선)
                        MailSentLogVO sendVo = new MailSentLogVO();
                        sendVo.setEmpId(userId);
                        sendVo.setMailId(message.getId());
                        sendVo.setToEmail(toEmail);
                        sendVo.setMailSubject(subject);
                        sendVo.setMailDate(mailDate);
                        // 필요에 따라 추가 필드도 설정
                        sentMailList.add(sendVo);
                    } else if (labelIds.contains("INBOX")) {
                        // INBOX 라벨이 있는 경우 - 받은 메일로 처리
                        MailReceptionVO vo = new MailReceptionVO();
                        vo.setEmpId(userId);
                        vo.setMailId(message.getId());
                        vo.setMailSubject(subject);
                        vo.setFromEmail(fromEmail);
                        vo.setMailDate(mailDate);
                        receptionMailList.add(vo);
                    }
                } else {
                    // 라벨 정보가 없는 경우 기본적으로 수신 메일로 처리
                    MailReceptionVO vo = new MailReceptionVO();
                    vo.setEmpId(userId);
                    vo.setMailId(message.getId());
                    vo.setMailSubject(subject);
                    vo.setFromEmail(fromEmail);
                    vo.setMailDate(mailDate);
                    receptionMailList.add(vo);
                }
            }
        }
        
        // 수신 메일 리스트를 mail_reception 테이블에 저장
        for (MailReceptionVO mail : receptionMailList) {
            try {
                mailMapper.insertMailList(mail);
            } catch (DuplicateKeyException e) {
                log.info("중복된 mailId로 인해 삽입 생략됨: {}", mail.getMailId());
            }
        }
        
        // 발신 메일 리스트를 sent_log 테이블에 저장
        for (MailSentLogVO mail : sentMailList) {
            try {
                mailMapper.insertMailLog(mail);
            } catch (DuplicateKeyException e) {
                log.info("중복된 mailId로 인해 삽입 생략됨: {}", mail.getMailId());
            }
        }
    }



    
    /**
     *	메일 상세보기 가져오깅 체킁!
     *	mailId, empId 필요 (구분자)
     *	실패시에 알림창 보여줍니당!
     */
    @Override
    public MailReceptionVO getMailContent(String userId, String mailId) throws Exception {
        Gmail service = mailConfig.getGmailService(userId);
        Message message = service.users().messages().get("me", mailId).setFormat("full").execute();
        String content = extractSentMailContent(message.getPayload());
        List<AttachmentDTO> attachments = extractAttachments(message.getPayload(), mailId, service);
        MailReceptionVO mail = new MailReceptionVO();
        mail.setMailId(mailId);
        mail.setMailBody(content);
        mail.setAttachment(attachments);
        return mail;
    }
    
    @Override
    public MailSentLogVO getSentMailContent(String userId, String mailId) throws Exception {
        Gmail service = mailConfig.getGmailService(userId);
        Message message = service.users().messages().get("me", mailId).setFormat("full").execute();
        String content = extractSentMailContent(message.getPayload());
        List<AttachmentDTO> attachments = extractAttachments(message.getPayload(), mailId, service);
        MailSentLogVO mail = new MailSentLogVO();
        mail.setMailId(mailId);
        mail.setSentCotentLog(content);
        mail.setAttachment(attachments);
        return mail;
    }
    
    public List<AttachmentDTO> extractAttachments(MessagePart part, String messageId, Gmail service) throws IOException {
        List<AttachmentDTO> attachments = new ArrayList<>();
        
        if (part == null) {
            log.info("extractAttachments: part is null for messageId: {}", messageId);
            return attachments;
        }
        
        log.info("extractAttachments: Processing part with mimeType: {} and filename: {}", part.getMimeType(), part.getFilename());
        
        // 파일명이 존재하고, 첨부파일 ID가 있다면 첨부파일로 판단
        if (part.getFilename() != null && !part.getFilename().isEmpty() 
                && part.getBody() != null && part.getBody().getAttachmentId() != null) {
            String attachmentId = part.getBody().getAttachmentId();
            log.info("extractAttachments: Found attachment - filename: {}, attachmentId: {}", part.getFilename(), attachmentId);
            MessagePartBody attachPart = service.users().messages().attachments()
                    .get("me", messageId, attachmentId)
                    .execute();
            AttachmentDTO attachment = new AttachmentDTO();
            attachment.setAttachmentId(attachmentId);
            attachment.setFilename(part.getFilename());
            attachment.setMimeType(part.getMimeType());
            attachment.setData(attachPart.getData()); // data는 base64url 인코딩된 값
            attachments.add(attachment);
        }
        
        // 서브파트가 있다면 재귀적으로 탐색
        if (part.getParts() != null && !part.getParts().isEmpty()) {
            log.info("extractAttachments: Found {} subParts. Processing subParts...", part.getParts().size());
            for (MessagePart subPart : part.getParts()) {
                attachments.addAll(extractAttachments(subPart, messageId, service));
            }
        } else {
            log.info("extractAttachments: No subParts found for part with filename: {}", part.getFilename());
        }
        
        return attachments;
    }


    private String extractSentMailContent(MessagePart part) throws IOException {
        String mimeType = part.getMimeType();
        StringBuilder content = new StringBuilder();

        if ("text/plain".equalsIgnoreCase(mimeType) || "text/html".equalsIgnoreCase(mimeType)) {
            String encodedData = part.getBody().getData();
            if (encodedData != null) {
                encodedData = encodedData.replaceAll("\\s+", "");
                byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedData);
                content.append(new String(decodedBytes, StandardCharsets.UTF_8));
            }
        } else if (part.getParts() != null) {
            for (MessagePart subPart : part.getParts()) {
                content.append(extractSentMailContent(subPart));
            }
        }
        return content.toString();
    }

    private String getMimeContent(MessagePart part) {
        String mimeType = part.getMimeType();

        if ("text/html".equalsIgnoreCase(mimeType)) {
            String encodedData = part.getBody().getData();
            if (encodedData != null) {
                encodedData = encodedData.replaceAll("\\s+", "");
                byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedData);
                return new String(decodedBytes, StandardCharsets.UTF_8);
            }
        }

        if (part.getParts() != null) {
            for (MessagePart subPart : part.getParts()) {
                String result = getMimeContent(subPart);
                if (result != null && !result.isEmpty()) {
                    return result;
                }
            }
        }

        return "";
    }

    @Override
    public List<String> getFavoriteMailIds(String empId) {
        return mailMapper.selectFavoriteMailIds(empId);
    }

    @Override
    public void addFavorite(String empId, String mailId) {
        Map<String, Object> map = new HashMap<>();
        map.put("empId", empId);
        map.put("mailId", mailId);
        map.put("mailFav", 1);
        mailMapper.insertFavorite(map);
    }

    @Override
    public void removeFavorite(String empId, String mailId) {
        Map<String, Object> map = new HashMap<>();
        map.put("empId", empId);
        map.put("mailId", mailId);
        map.put("mailFav", 0);
        mailMapper.deleteFavorite(map);
    }

    @Override
    public List<MailReceptionVO> getFavoriteMailList(String empId) {
        return mailMapper.selectFavoriteMailList(empId);
    }

    @Override
    public MailUserAuthVO getToken(String userId) {
        return mailMapper.selectTokenByUserId(userId);
    }

    @Override
    public void saveOrUpdateToken(MailUserAuthVO token) {
        MailUserAuthVO existing = mailMapper.selectTokenByUserId(token.getEmpId());
        if (existing == null) {
            mailMapper.insertToken(token);
        } else {
            mailMapper.updateToken(token);
        }
    }
    
    @Override
    public void tryAutoAuth(String empId) {
        MailUserAuthVO token = mailMapper.selectTokenByUserId(empId);

        if (token == null) {
            // Gmail 인증 필요: 예외로 핸들링
            throw new NeedOAuthRedirectException("최초 사용자, Gmail 인증이 필요합니다.");
        }

        if (token.getTokenExpiry().before(new Date())) {
            // 토큰 만료 시 재갱신 (리프레시 토큰 사용)
            this.saveOrUpdateToken(token);
        }
    }

	@Override
	public List<MailReceptionVO> selectMailList(String empId) {
		return mailMapper.selectMailList(empId);
	}
	
	@Override
	public List<MailSentLogVO> selectSendMailList(String empId) {
		return mailMapper.selectSendMailList(empId);
	}
	
	@Override
	public void sendMailWithLog(MailSentLogVO vo, MultipartFile[] attachments) throws Exception {
	    Gmail service = mailConfig.getGmailService(vo.getEmpId());

	    // Gmail 전송을 위한 메일 객체 생성
	    MimeMessage email;
	    if (attachments != null && attachments.length > 0) {
	        email = createEmailWithAttachments(
	                vo.getToEmail(),
	                vo.getEmpId(),
	                vo.getMailSubject(),
	                vo.getSentCotentLog(),
	                attachments
	        );
	    } else {
	        email = createEmail(vo.getToEmail(),
	                vo.getEmpId(),
	                vo.getMailSubject(),
	                vo.getSentCotentLog());
	    }

	    Message message = createMessageWithEmail(email);
	    Message result = service.users().messages().send("me", message).execute();

	    // Gmail 메일 ID 저장
	    vo.setMailId(result.getId());

	    // 현재 시간 저장 (Asia/Seoul, nano 제거)
	    LocalDateTime nowWithoutNano = LocalDateTime.now(ZoneId.of("Asia/Seoul")).withNano(0);
	    vo.setMailDate(Timestamp.valueOf(nowWithoutNano));

	    // 로그 DB 저장
	    mailMapper.insertMailLog(vo);
	}
	
	private MimeMessage createEmailWithAttachments(String to, String from, String subject, String bodyText, MultipartFile[] attachments) throws MessagingException, IOException {
	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);
	    MimeMessage email = new MimeMessage(session);

	    email.setFrom(new InternetAddress(from));
	    email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
	    email.setSubject(subject, "UTF-8");

	    // 본문 부분 MimeBodyPart 생성
	    MimeBodyPart textPart = new MimeBodyPart();
	    textPart.setContent(bodyText, "text/html; charset=UTF-8");

	    // 전체 Multipart 생성 후 본문 부분 추가
	    MimeMultipart multipart = new MimeMultipart();
	    multipart.addBodyPart(textPart);

	    // 첨부파일이 있다면 각각의 파일을 Multipart에 추가
	    for (MultipartFile file : attachments) {
	        if (file != null && !file.isEmpty()) {
	            MimeBodyPart attachmentPart = new MimeBodyPart();
	            String originalFilename = file.getOriginalFilename();
	            // 첨부파일 이름을 UTF-8로 인코딩하여 설정 (한글 지원)
	            attachmentPart.setFileName(originalFilename);
	            
	            DataSource dataSource = new ByteArrayDataSource(file.getBytes(), file.getContentType());
	            attachmentPart.setDataHandler(new javax.activation.DataHandler(dataSource));
	            multipart.addBodyPart(attachmentPart);
	        }
	    }
	    
	    email.setContent(multipart);
	    return email;
	}
	
	private MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);

	    MimeMessage email = new MimeMessage(session);
	    email.setFrom(new InternetAddress(from));
	    email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
	    email.setSubject(subject, "UTF-8");
	    email.setText(bodyText, "UTF-8");
	    return email;
	}
	
	private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    emailContent.writeTo(buffer);
	    byte[] rawMessageBytes = buffer.toByteArray();
	    String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

	    Message message = new Message();
	    message.setRaw(encodedEmail);
	    return message;
	}




	
}
