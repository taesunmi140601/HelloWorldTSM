package kr.or.ddit.works.community.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 댓글 COMMENT VO
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
public class CommentVO implements Serializable{

	private Long replyNo;      			//댓글 번호
	private Long postNo;      			//게시글 번호
	private Long memberNo;      		//멤버 번호
	private String replyContent;     	//댓글 내용
	private String replyCreateDate;     //댓글 작성일
	private String replyUpdateDate;     //댓글 수정일

	private PostVO post;
	private CommunityMemberVO communityMember;
}
