package kr.or.ddit.works.vote.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 투표 문항 VOTE_QUESTIONS VO
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
public class VoteQuestionsVO implements Serializable{
	
	private Long voteQuestionNo;      			//투표 문항 번호
	private Long voteNo;      					//투표 번호
	private String voteQuestionContent;      	//문항 내용
	private String voteIsRequired;      		//필수 문항 여부
	private String voteOptions;      			//선택지

}
