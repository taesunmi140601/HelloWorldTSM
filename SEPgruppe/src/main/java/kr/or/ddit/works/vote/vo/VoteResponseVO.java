package kr.or.ddit.works.vote.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 투표 응답 VOTE_RESPONSE VO
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
public class VoteResponseVO implements Serializable{
	
	private Long voteResponseNo;      		//투표 응답 번호
	private Long voteQuestionNo;      		//투표 문항 번호
	private String empId;      				//투표 응답자
	private String voteResponseContent;     //응답 내용
	private String voteResponseTimestamp;   //응답 일시

}
