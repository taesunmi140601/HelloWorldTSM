package kr.or.ddit.works.mail.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.MessagePart;

import kr.or.ddit.works.mail.vo.AttachmentDTO;
import kr.or.ddit.works.mail.vo.MailReceptionVO;
import kr.or.ddit.works.mail.vo.MailSentLogVO;
import kr.or.ddit.works.mail.vo.MailUserAuthVO;

/**
 * 메일 서비스
 * @author JYS
 * @since 2025. 3. 16.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 16.     	JYS	          최초 생성
 *
 * </pre>
 */
@Service
public interface MailService {
	void getMailList(String userId) throws Exception; //받은메일함 조회
	
	
	
	MailReceptionVO getMailContent(String userId, String mailId) throws Exception; // 받은 메일 내용 조회
	
	MailSentLogVO getSentMailContent(String userId, String mailId) throws Exception; // 보낸 메일 내용 조회
	
	List<String> getFavoriteMailIds(String empId); // 즐겨찾기 메일 조회

	void addFavorite(String empId, String mailId) throws Exception;
	
    void removeFavorite(String empId, String mailId) throws Exception;
    
    List<MailReceptionVO> getFavoriteMailList(String empId);
    
    public List<MailReceptionVO> selectMailList(String empId);
    
    public List<MailSentLogVO> selectSendMailList(String empId);
    
    public MailUserAuthVO getToken(String userId);
    
    public void saveOrUpdateToken(MailUserAuthVO token);

    public void tryAutoAuth(String empId);

    public void sendMailWithLog(MailSentLogVO vo, MultipartFile[] attachments) throws Exception;
    
    public List<AttachmentDTO> extractAttachments(MessagePart part, String messageId, Gmail service) throws IOException;
}
