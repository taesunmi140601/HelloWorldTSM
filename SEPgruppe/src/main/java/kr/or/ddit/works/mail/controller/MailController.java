package kr.or.ddit.works.mail.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import kr.or.ddit.works.mail.config.GMailConfig;
import kr.or.ddit.works.mail.service.MailService;
import kr.or.ddit.works.mail.vo.AttachmentDTO;
import kr.or.ddit.works.mail.vo.MailReceptionVO;
import kr.or.ddit.works.mail.vo.MailSentLogVO;

@Controller
@RequestMapping("/{companyNo}/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @Lazy
    @Autowired
    private GMailConfig mailConfig;
    
    @GetMapping("")
    public String getMailList(@PathVariable("companyNo") String companyNo,
                               Authentication authentication,
                               Model model) throws Exception {
        String empId = authentication.getName();
        List<MailReceptionVO> mailList = mailService.selectMailList(empId);
        model.addAttribute("mailList", mailList);
        model.addAttribute("companyNo", companyNo);
        return "gw:mail/mailHome";
    }
    
    @GetMapping("/{type:list|mySentMail}")
    public String getMailList(@PathVariable("companyNo") String companyNo,
                              @PathVariable("type") String type,
                              Authentication authentication,
                              Model model) throws Exception {

        String empId = authentication.getName();

        if ("mySentMail".equals(type)) {
            List<MailSentLogVO> sentMailList = mailService.selectSendMailList(empId);
            model.addAttribute("mailList", sentMailList);
            return "groupware/mail/sentList";
        } else {
            List<MailReceptionVO> mailList = mailService.selectMailList(empId);
            model.addAttribute("mailList", mailList);
            return "groupware/mail/mailList";
        }
    }

    @GetMapping("/{type:mailContent|sentMailContent}/{mailId}")
    public String getAnyMailContent(@PathVariable String companyNo,
                                    @PathVariable String mailId,
                                    @PathVariable String type,
                                    Authentication authentication,
                                    Model model) throws Exception {

        String empId = authentication.getName();
        MailSentLogVO sentDetail;
        MailReceptionVO inboxDetail;

        if ("sentMailContent".equals(type)) {
        	sentDetail = mailService.getSentMailContent(empId, mailId);
            model.addAttribute("sentMailContent", sentDetail);
            return "groupware/mail/sentMailContent";
        } else {
        	inboxDetail = mailService.getMailContent(empId, mailId);
            model.addAttribute("mailContent", inboxDetail);
            return "groupware/mail/mailContent";
        }
    }

    @GetMapping("/favorites")
    public String showFavoriteMailPage(@PathVariable("companyNo") String companyNo,
                                       Authentication authentication,
                                       Model model) {
        String empId = authentication.getName();
        List<MailReceptionVO> favoriteList = mailService.getFavoriteMailList(empId);
        model.addAttribute("mailList", favoriteList);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String mailListJson = mapper.writeValueAsString(favoriteList);
            model.addAttribute("mailListJson", mailListJson);
        } catch (JsonProcessingException e) {
            model.addAttribute("mailListJson", "[]");
        }

        model.addAttribute("companyNo", companyNo);
        return "gw:mail/mailFavorite";
    }

    @GetMapping("/favorites/list")
    @ResponseBody
    public List<MailReceptionVO> getFavoriteMailList(@PathVariable("companyNo") String companyNo,
                                                     Authentication authentication) {
        String empId = authentication.getName();
        return mailService.getFavoriteMailList(empId);
    }

    @PostMapping("/favorites/add")
    @ResponseBody
    public String addFavorite(@PathVariable("companyNo") String companyNo,
                              @RequestParam String mailId,
                              Authentication authentication) throws Exception {
        String empId = authentication.getName();
        mailService.addFavorite(empId, mailId);
        return "success";
    }

    @PostMapping("/favorites/remove")
    @ResponseBody
    public String removeFavorite(@PathVariable("companyNo") String companyNo,
                                 @RequestParam String mailId,
                                 Authentication authentication) throws Exception {
        String empId = authentication.getName();
        mailService.removeFavorite(empId, mailId);
        return "success";
    }
    
    @GetMapping("/send")
    public String mailSendForm(
    	@PathVariable("companyNo") String companyNo
    	, Model model
    ) {
    	model.addAttribute("companyNo", companyNo);
    	return "groupware/mail/mailSendForm";
    }
    
    @PostMapping("/send")
    @ResponseBody
    public String sendMail(@PathVariable String companyNo,
                           @ModelAttribute MailSentLogVO mailLogVO,
                           @RequestParam(value = "attachments", required = false) MultipartFile[] attachments,
                           Authentication authentication) {
        String fromEmpId = authentication.getName();
        mailLogVO.setEmpId(fromEmpId); // 로그인 사용자 ID 설정

        try {
            // 첨부파일 배열을 서비스로 전달합니다.
            mailService.sendMailWithLog(mailLogVO, attachments);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    
    @GetMapping("/attachmentDownload")
    public ResponseEntity<InputStreamResource> downloadAttachment(
            @RequestParam String mailId,
            @RequestParam String filename,    // 파일명을 파라미터로 받음
            Authentication authentication) throws Exception {
        
        String empId = authentication.getName();
        Gmail service = mailConfig.getGmailService(empId);
        Message message = service.users().messages().get("me", mailId).setFormat("full").execute();
        
        // 파일명을 기준으로 첨부파일 조회
        AttachmentDTO attachment = findAttachmentDTOByFilename(message, mailId, filename, service);
        if (attachment == null) {
            return ResponseEntity.notFound().build();
        }
        
        byte[] fileBytes = Base64.getUrlDecoder().decode(attachment.getData());
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(fileBytes));
        
        
        // 파일명을 UTF-8로 URL 인코딩하여 지정 (RFC 5987 기준)
        String encodedFilename = URLEncoder.encode(attachment.getFilename(), StandardCharsets.UTF_8.toString())
                                    .replaceAll("\\+", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename);
        
        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(attachment.getMimeType());
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileBytes.length)
                .contentType(mediaType)
                .body(resource);
    }

    /**
     * 파일명을 기준으로 AttachmentDTO를 찾는 메서드
     */
    private AttachmentDTO findAttachmentDTOByFilename(Message message, String mailId, String filename, Gmail service) throws IOException {
        // 메일 페이로드에서 첨부파일 목록을 재추출 (기존 extractAttachments() 사용)
        List<AttachmentDTO> attachments = mailService.extractAttachments(message.getPayload(), mailId, service);
        for (AttachmentDTO att : attachments) {
            if (att.getFilename() != null && att.getFilename().equals(filename)) {
                return att;
            }
        }
        return null; // 해당 파일명이 발견되지 않은 경우 null 반환
    }


} 
