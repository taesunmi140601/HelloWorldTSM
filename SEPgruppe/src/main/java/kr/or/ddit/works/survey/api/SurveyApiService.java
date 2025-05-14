package kr.or.ddit.works.survey.api;

import java.util.List;
import java.util.Map;

import kr.or.ddit.works.survey.vo.SurveyResponseVO;
import kr.or.ddit.works.survey.vo.SurveyVO;

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
public interface SurveyApiService {

    // SurveyMonkey 설문 생성
    boolean createSurveyOnApi(SurveyVO surveyVO);

    // SurveyMonkey 설문 상세 조회
    SurveyVO getSurveyDetailsFromApi(String surveyId);

    // SurveyMonkey 응답 제출
    boolean submitSurveyResponseToApi(String surveyId, SurveyResponseVO responseVO);

    // 기타 필요한 API 메서드들 추가 가능
    String createSurvey(String jsonBody) throws Exception;

	List<SurveyVO> getAllSurveys();
	
	Map<String, Integer> getSurveyResponseCounts(List<String> surveyIds);
}
