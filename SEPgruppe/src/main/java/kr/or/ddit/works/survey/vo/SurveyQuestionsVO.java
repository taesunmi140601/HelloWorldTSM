package kr.or.ddit.works.survey.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 설문문항 SURVEY_QUESTIONS VO
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
public class SurveyQuestionsVO implements Serializable{

	private Long surveyQuestionNo;      			//설문 문항 번호
	private Long surveyNo;      					//설문 번호
	private String surveyQuestionContent;      		//문항 내용
	private String surveyIsRequired;      			//필수 문항 여부
	private String surveyQuestionType;      		//문항 유형
	private String surveyOptions;      				//선택지
	private String surveyAdditionalExplanation;     //추가 설명
	
	private Long id;             // 질문 ID (PK)
    private String surveyId;     // 설문 ID (FK)
    private String questionText; // 질문 내용
    private String questionType; // 질문 유형 (e.g., single_choice, multiple_choice, open_ended)
}
