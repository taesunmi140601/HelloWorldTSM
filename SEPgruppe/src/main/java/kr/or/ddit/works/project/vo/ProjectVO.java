package kr.or.ddit.works.project.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 프로젝트 PROJECT VO
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
public class ProjectVO implements Serializable {
    private Long projectNo;               // 프로젝트 번호
    private String empId;                 // 사원 아이디
    private String projectTitle;          // 프로젝트 제목
    private Date projectStartTime;        // 프로젝트 시작일
    private Date projectEndTime;          // 프로젝트 종료일
    private String projectStatus;         // 프로젝트 상태
    private String projectDesc;           // 프로젝트 설명
    private String empNm;                 // 사원 이름
    private String projectOwnerId;		  // 프로젝트 생성자

    // 참여자 정보
    private List<Participant> participants; // 참여자 정보
    
    // 일감 정보
    private List<Task> tasks;             // 일감 리스트

    @Data
    public static class Participant {
    	private Long projectParticipantNo;
        private String empId;   // 참여자 ID
        private String empNm;   // 참여자 이름
        private long projectNo; // 프로젝트 번호 추가

        // 기본 생성자
        public Participant() {}

        // 생성자
        public Participant(String empId, String empNm) {
            this.empId = empId;
            this.empNm = empNm;
        }

        // 프로젝트 번호 설정
        public void setProjectNo(long projectNo) {
            this.projectNo = projectNo;
        }
    }

    @Data
    public static class Task {
        private Long taskNo;                // 업무 번호
        private String taskTitle;           // 업무 제목
        private String progress;            // 진행률
        private String priority;            // 우선순위
    }
}





