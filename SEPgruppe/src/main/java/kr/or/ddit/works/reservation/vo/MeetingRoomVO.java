package kr.or.ddit.works.reservation.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 회의실 MEETINGROOM VO
 *
 * @author JYS
 * @since 2025. 3. 13.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 13.     	JYS	          최초 생성
 *  2025. 3. 14. 		KKM			  개발 시작
 * </pre>
 */
@Data
public class MeetingRoomVO implements Serializable {
	private long roomNo;      			//회의실 번호
	private String roomNm;      		//회의실 이름
	private String roomLocation;		//회의실 위치
	private String roomAvailability;	//예약 가능 여부
	private long roomCapacity;			//수용 인원
}
