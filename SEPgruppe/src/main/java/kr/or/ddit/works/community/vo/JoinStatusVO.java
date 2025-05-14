package kr.or.ddit.works.community.vo;

import java.io.Serializable;
import java.util.List;

import kr.or.ddit.works.organization.vo.EmployeeVO;
import lombok.Data;

/**
 * 가입상태 JOIN_STATUS VO
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
public class JoinStatusVO implements Serializable {

	private Long requestNo;      		//가입 요청번호
	private Long communityNo;      		//방 번호
	private String empId;      			//사원 아이디
	private String userStatus;      	//가입 상태(:승인,대기,거절)
	private String requestDate;     	//가입 신청일
	private String rejectionReason;     //거절사유

	private List<CommunityMemberVO> communityMember;
	private CommunityVO community;
	private EmployeeVO employee;
}
