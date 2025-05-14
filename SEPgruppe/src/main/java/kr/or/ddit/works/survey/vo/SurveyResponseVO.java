package kr.or.ddit.works.survey.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 설문 응답 SURVEY_RESPONSE VO
 * 
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
public class SurveyResponseVO implements Serializable  {
	
	private Long surveyResponseNo;      		//설문 응답 번호
	private Long surveyQuestionNo;      		//설문 문항 번호
	private String empId;      					//설문 응답자
	private String surveyResponseContent;      	//응답 내용
	private String surveyResponseTimestamp;     //응답 일시
	
	private String surveyId;
    private List<QuestionResponse> questions;

    @Data
    public static class QuestionResponse {
        private String surveyId;
        private String questionId;
        private List<String> answers;
        
        public QuestionResponse() {
        	
        } 

        public QuestionResponse(String questionId, List<String> answers) {
            this.questionId = questionId;
            this.answers = answers;
        }
    }
}
