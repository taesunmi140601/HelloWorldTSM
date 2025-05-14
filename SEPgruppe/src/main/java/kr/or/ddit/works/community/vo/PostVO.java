package kr.or.ddit.works.community.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 게시글 POST VO
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
public class PostVO implements Serializable {

	private Long postNo;      			//게시글 번호
	private Long boardNo;      			//게시판 번호
	private Long memberNo;      		//멤버 번호
	private String postTitle;     		//게시글 제목
	private String postContent;      	//게시글 내용
	private String postCreatedAt;      	//게시글 작성일
	private String postUpdatedAt;      	//게시글 수정일
	private Long postLikeCount;      	//좋아요 수
	private String postIsDraft;      	//임시저장 여부(Y/N)
	private String postAllowComments;   //댓글 작성 허용 여부(Y/N)
	private Long fileGroupNo;      		//파일그룹번호
	private String empId;				//게시글 작성자 아이디
	private Long postViewCount;			//조회수

	private List<CommentVO> comments;
	private CommunityMemberVO communityMember;
	private BoardVO board;
	private List<LikeVO> likes;

}
