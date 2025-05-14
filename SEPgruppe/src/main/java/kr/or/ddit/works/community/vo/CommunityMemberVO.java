package kr.or.ddit.works.community.vo;

import java.io.Serializable;
import java.util.List;

import kr.or.ddit.works.organization.vo.EmployeeVO;
import lombok.Data;


/**
 * 커뮤니티 멤버 COMMUNITY_MEMBER VO
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
public class CommunityMemberVO implements Serializable {

	private Long memberNo;      			//멤버 번호
	private Long requestNo;      			//가입 요청번호
	private Long communityNo;      			//방 번호
	private String empId;      				//사원 아이디
	private String memberJoinDate;      	//가입일
	private String memberRole;      		//예:방 주인,멤버
	private String memberActivityStatus;    //(예:활동중,탈퇴,정지)

	private EmployeeVO employee;
	private List<PostVO> post;
	private List<CommentVO> comments;
	private List<LikeVO> likes;
	private CommunityVO community;
	private JoinStatusVO joinStatus;

}
