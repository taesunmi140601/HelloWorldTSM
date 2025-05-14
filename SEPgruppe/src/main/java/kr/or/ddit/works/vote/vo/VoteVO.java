package kr.or.ddit.works.vote.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 투표 VOTE VO
 * @author JYS
 * @since 2025. 3. 15.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 15.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
public class VoteVO implements Serializable{
	
	private Long voteNo;      			//투표 번호
	private String voteTitle;      		//투표 제목
	private String voteContent;      	//투표 내용
	private String voteWriteTime;      	//투표 생성일
	private String voteStartTime;      	//투표 시작일
	private String voteEndTime;      	//투표 종료일
	private String voteAnonymous;      	//익명 응답 여부
	private String voteTargetScope;     //대상 범위 (부서, 팀 등)
	private String empId;      			//투표 생성자

}
