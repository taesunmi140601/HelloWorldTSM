package kr.or.ddit.works.survey.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 설문 SURVEY VO
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
/**
 * @author SJH
 * @since 2025. 3. 25.
 */
@Data
public class SurveyVO implements Serializable {

    // ✅ 기존 DB 기반 필드
    private Long surveyNo;      			// 설문 번호
//    private String surveyTitle;      		// 설문 제목
    private String surveyContent;     		// 설문 설명
    private String surveyWriteTime;      	// 설문 생성일
    private String surveyStartTime;      	// 설문 시작일
    private String surveyEndTime;      		// 설문 종료일
    private String surveyType;      		// 설문 유형
    private String surveyAnonymous;      	// 익명 응답 여부
    private String surveyTargetScope;      	// 대상 범위 (부서, 팀 등)
    private String empId;      				// 사원 아이디

    // ✅ SurveyMonkey API용 필드 추가
    private String id;                      // SurveyMonkey에서 부여하는 설문 ID
    private String closed;					// 설문 마감 여부
    private List<QuestionVO> questions;		// 설문 질문 목록
    private String title; 					// 설문 제목
    private String surveyId;
    private String dateCreated;   // 설문 생성일 (SurveyMonkey API에서 가져옴)
    private String creator;       // 설문 생성자 (우리 시스템에서 관리)

    
    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }

    // ✅ SurveyMonkey 질문 구조를 내부 클래스로 정의
    @Data
    public static class QuestionVO {
		 private String heading;
	     private String family;             // e.g., single_choice
	     private List<String> choices;      // 선택지 목록
	     private String choicesCsv;         // JSP 폼 입력용 CSV 문자열
    }
}