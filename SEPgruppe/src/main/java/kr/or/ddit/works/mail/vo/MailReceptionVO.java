package kr.or.ddit.works.mail.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 메일 수신 MAIL_RECEPTION VO
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
public class MailReceptionVO implements Serializable {
	
	private Long mailRecpNo;      		//메일 수신 번호
	private String empId;      			//사원 아이디
	private Long mailCategoryNo;      	//메일 카테고리 번호
	private String mailId;      		//메일 아이디
	private String fromEmail;      		//발신자 이메일 주소
	private String toEmail;      		//수신자 이메일 주소
	private String ccEmail;      		//참조 수신자 이메일 주소
	private String mailSubject;      	//메일 제목
	private String mailBody;      		//메일 본문
	private Long fileGroupNo;      		//파일그룹번호
	private Timestamp mailDate;      		//메일 수신 날짜
	
	private String formattedDate;
	private int mailFav;      		//즐겨찾기 여부
	
	private List<AttachmentDTO> attachment;

}
