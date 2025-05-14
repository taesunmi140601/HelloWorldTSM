package kr.or.ddit.works.mail.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

/**
 * 발송 기록 테이블 MAIL_SENT_LOG VO
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
@Data
public class MailSentLogVO implements Serializable {
	
	private Long mailSentNo;      	//메일 발신 번호
	private String empId;      		//사원 아이디
	private String mailId;      	//메일 아이디
	private String toEmail;      	//수신자 이메일 주소
	private String ccEmail;      	//참조 수신자 이메일 주소
	private String bccEmail;      	//숨은 참조 이메일 주소
	private String mailSubject;     //메일 제목
	private String sentCotentLog;   //메일 본문
	private Long fileGroupNo;      	//파일그룹번호
	private Timestamp mailDate;      	//메일 발송 시간

	private List<AttachmentDTO> attachment;
	
}
