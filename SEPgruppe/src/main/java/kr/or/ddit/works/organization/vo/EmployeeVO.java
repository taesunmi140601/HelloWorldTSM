package kr.or.ddit.works.organization.vo;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.validate.InsertGroup;
import kr.or.ddit.works.community.vo.CommunityVO;
import kr.or.ddit.works.login.vo.AllUserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 사원 EMPLOYEE VO
 * @author JYS
 * @since 2025. 3. 12.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 12.     	JYS	          최초 생성
 *
 * </pre>
 */

@ToString(callSuper = true)
@EqualsAndHashCode(of="empId")
@Data
public class EmployeeVO extends AllUserVO implements Serializable {
	@NotBlank(groups = InsertGroup.class)
	private String empId;      		// 사원 아이디
	@NotBlank(groups = InsertGroup.class)
	private String companyNo;      	// 고객사번호
	private String positionCd;      	// 직위코드
	private String positionName;	// 직책이름
	private String deptCd;      	// 부서코드
	private String deptName;		// 부서이름
	@NotBlank(groups = InsertGroup.class)
	private String empNo;      		// 사원번호
	@NotBlank(groups = InsertGroup.class)
	private String empNm;     		// 사원이름
	@Size(min = 4, max = 8)
	private String empPw; 			// 사원 비밀번호
	@NotBlank
	private String empZip;      	// 우편번호
	@NotBlank
	private String empAdd1;      	// 주소
	private String empAdd2;      	// 상세주소
	private String empEmail;      	// 이메일
	private String empPhone;      	// 사원 전화번호
	private String empRegdate;      // 입사년도
	private String empImg;			// 파일 경로
	private String empBank;			// 은행명
	private String empDepositor;	// 예금주
	private String empBankno;		// 계좌번호
	private String empRetire;		// 퇴사여부
	private String empGender;		// 성별

	private MultipartFile attachFile; 	// 클라이언트의 업로드 이미지를 받기 위한 프로퍼티

	private List<CommunityVO> community;
	public void setAttachFile(MultipartFile attachFile) {
		if(attachFile == null || attachFile.isEmpty()) {
			return;
		}else {
			this.attachFile = attachFile;
			this.empImg = UUID.randomUUID().toString();
		}

	}
}
