package kr.or.ddit.works.notice.vo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.validate.InsertGroup;
import kr.or.ddit.validate.UpdateGroup;
import kr.or.ddit.works.attachFile.vo.AttachFileVO;
import kr.or.ddit.works.attachFile.vo.PostAttachFileVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.organization.vo.OrganizationVO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 공지사항 NOTICE VO
 * @author JYS
 * @since 2025. 3. 14.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 14.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
@EqualsAndHashCode(of="noticeNo")
public class NoticeVO implements Serializable {

	private int noticeNo;      			//공지사항 번호
	private String empId;      			//사원 아이디
	private String noticeCategory;      //공지사항 카테고리
	@NotBlank(message = "제목은 공백일 수 없습니다.")
	private String noticeTitle;      	//제목
	@NotBlank(message = "내용은 공백일 수 없습니다.")
	private String noticeContent;      	//내용
	private String noticeCreatedAt;     //작성일
	private String noticeUpdatedAt;     //수정일
	@Min(0)
	private Long noticeViewCount;      	//조회수
	private char isDraft;      			//임시저장 여부
	private Long fileGroupNo;      		//파일그룹번호
	private String companyNo;			// 고객사 번호
	// 게시글 전체 조회를 위한 프로퍼티
	private int rnum;
	private String empNm;				//글 작성자명
	private String positionName;		//글 작성자직급
	private String deptName;			//부서명
	private String deptCd;				//부서코드
	private String category;			//카테고리

	// 게시글 등록 권한 설정을 위한 프로퍼티
	private String parentDeptCd;
	private String managerEmpId;
	private String createAt;

	private OrganizationVO member;		// HAS A 관계
	private List<AttachFileVO> file;	// HAS MANY 관계
	private List<PostAttachFileVO> postAttachFileVo;

}

