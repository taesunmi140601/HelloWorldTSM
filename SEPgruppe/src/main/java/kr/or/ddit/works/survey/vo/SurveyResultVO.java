package kr.or.ddit.works.survey.vo;

import java.util.List;

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
public class SurveyResultVO {
    private String surveyId;
    private String title;           // optional: 설문 제목
    private String description;     // optional: 설문 설명

    private List<SurveyQuestionStatVO> questionStats;
  
}
