package kr.or.ddit.works.company.vo;

import java.io.Serializable;

/**
 * 고객사 알람 COMPANIES_ALARM VO
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
public class CompanyAlarmVO implements Serializable {
	
	private Long key;      				//알람 번호
	private String contactId;      		//고객사 아이디
	private String alarmNm;      		//알람 제목
	private String alarmContent;      	//알람 내용
	private String alarmCategoryNo;     //알람 분류(승인 요청, 일정 알림, 일반 공지)
	private String isAlarmRead;      	//읽음 상태
	private String alarmDate;      		//알람 발송 시간
	private String alarmReadTime;      	//읽은 시간

}
