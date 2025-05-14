package kr.or.ddit.works.schedule.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import kr.or.ddit.validate.InsertGroup;
import kr.or.ddit.works.organization.vo.OrganizationVO;
import lombok.Data;

/**
 * 일정 SCHEDULE VO
 * @author JYS
 * @since 2025. 3. 16.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 16.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
public class ScheduleVO implements Serializable {

	private Long schdlNo; 					// 일전순번

	@NotNull(groups = InsertGroup.class)
	private Long scheduleTypeNo; 			// 일정유형번호

	@NotBlank(groups = InsertGroup.class)
	private String empId; 					// 사원 아이디

	@NotBlank(groups = InsertGroup.class)
	private String schdlNm; 				// 일정명

	@NotBlank(groups = InsertGroup.class)
	private String schdlCn; 				// 일정설명

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date schdlBgngYmd; 				// 일정 시작일

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date schdlEndYmd; 				// 일정 종료일

	@NotBlank(groups = InsertGroup.class)
	private String schdlLocation; 			// 일정 장소

	private Date schdlCreateDate; 			// 최초 등록 일시

	@NotBlank(groups = InsertGroup.class)
	private String schdlStatus; 			// 일정상태 (참석/불참/확정/변경/취소)

	private String scheduleTypeNm; 			// 일정 분류명 (개인/부서/사내/프로젝트)

	private int cnt; 						// 일정 개수

    private String department;				// 부서명

    private Long projectNo;					// 프로젝트 번호

	private ScheduleTypeVO scheduleType;	// 1대1 관계
    private OrganizationVO employee;		// 1대1 관계



}
