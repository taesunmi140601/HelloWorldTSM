package kr.or.ddit.works.mail.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

/**
 * 메일 사용자 인증 및 토큰 관리 MAIL_USER_AUTHENTICATION VO
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
public class MailUserAuthVO implements Serializable{
	
	private String empId;      			//사원 아이디
	private String emailAdd;      		//이메일 주소
	private String accessToken;      	//Gmail 토큰
	private String refreshToken;      	//Gmail 갱신 토큰
	private String authType;      		//토큰 인증 유형
	private Timestamp tokenExpiry;     	//토큰 만료일
	private String firstLinkedAt;      	//최초 연동일시
	private String tokenLastUpdate;     //토큰 마지막 갱신

}
