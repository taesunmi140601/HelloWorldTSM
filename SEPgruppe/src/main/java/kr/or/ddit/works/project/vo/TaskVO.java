package kr.or.ddit.works.project.vo;

import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import kr.or.ddit.works.organization.vo.OrganizationVO;

/**
 * 일감 TASK VO
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
public class TaskVO implements Serializable{
	private long taskNo;      				//업무번호
	private Long projectNo;      			//프로젝트 번호
	private long projectParticipantNo;      //참여자 번호
	private String projectEmpId; 			// 프로젝트 생성자 ID
	private String empId;      				//일감 부여자
	private String taskTitle;      			//업무 제목
	private String taskContent;      		//업무 내용
	private String progress;      			//진행율
	private String importance;      		//중요도
	private String priority;      			//우선순위
	private Date taskCreateDate;      	//업무 생성일자
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date taskStartDate;      		//업무 시작일자
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date taskEndDate;      		//업무 마감일자
	
	private Date taskUpdateDate;      	//업무 수정일자
	private long fileGroupNo;      			//파일그룹번호
	
	private String projectTitle;
	private String empNm;
	
	private OrganizationVO member;		// HAS A 관계
	
	private ProjectVO project;
	
	private List<MultipartFile> uploadFiles; // 업로드된 파일들을 담을 필드
}
