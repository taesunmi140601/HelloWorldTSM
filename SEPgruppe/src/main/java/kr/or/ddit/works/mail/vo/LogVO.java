package kr.or.ddit.works.mail.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 로그 LOG VO
 * @author JYS
 * @since 2025. 3. 17.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 17.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
public class LogVO implements Serializable {
	
	private Long logNo;      		//로그번호
	private String empId;      		//사원 아이디
	private String logIp;      		//로그IP
	private String logContent;      //로그내용
	private String logDate;      	//로그시간

}
