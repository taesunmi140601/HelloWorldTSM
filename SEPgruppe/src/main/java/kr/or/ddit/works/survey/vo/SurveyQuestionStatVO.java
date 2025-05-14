package kr.or.ddit.works.survey.vo;

import java.util.List;
import java.util.Map;

import lombok.Data;
/**
 * 
 * @author SJH
 * @since 2025. 3. 24.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 24.     	SJH	          최초 생성
 *
 * </pre>
 */
@Data
public class  SurveyQuestionStatVO {
        private String questionId;
        private String questionText;
        private String answerText;
        private int count;
        private Map<String, Integer> answerCount; // 선택지 텍스트 → 응답 수
}
