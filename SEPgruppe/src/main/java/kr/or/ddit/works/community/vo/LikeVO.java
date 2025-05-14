package kr.or.ddit.works.community.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 좋아요 LIKE VO
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
public class LikeVO implements Serializable {

	private Long likeNo;      		//좋아요 번호
	private Long postNo;      		//게시글 번호
	private Long memberNo;     		//멤버 번호
	private String likeIsActive;    //좋아요 여부(Y/N)

	private PostVO post;
	private CommunityMemberVO communityMember;

}
