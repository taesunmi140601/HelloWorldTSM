package kr.or.ddit.works.community.vo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.organization.vo.OrganizationVO;
import lombok.Data;

/**
 * 커뮤니티 COMMUNITY VO
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
public class CommunityVO implements Serializable {
	private Integer memberCount;			//멤버 카운트
	private String joinedEmpId; 			//가입여부
	private Integer postCount;				//게시글 수
	private Long communityNo;      			//방 번호
	private String empId;      				//사원 아이디
	private String communityNm;      		//방 이름
	private String communityContent;      	//방 설명
	private String communityCreatedAt;      //방 생성일
	private String communityIsClosed;      	//공통
	private String closureReason;     		//폐쇠 사유
	private String closureTimestamp;      	//폐쇠 일시


	private List<CommunityMemberVO> communityMember;
	private List<JoinStatusVO> joinStatus;
	private EmployeeVO employee;
	private List<BoardVO> board;


}
