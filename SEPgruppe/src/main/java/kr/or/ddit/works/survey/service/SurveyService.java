package kr.or.ddit.works.survey.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.works.survey.vo.SurveyResponseVO;
import kr.or.ddit.works.survey.vo.SurveyResultVO;
import kr.or.ddit.works.survey.vo.SurveyVO;

/**
 * 설문 서비스
 * @author JYS
 * @since 2025. 3. 17.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 17.     	JYS	          최초 생성
 *
 * </pre>
 */
/**
 * @author SJH
 * @since 2025. 3. 25.
 */
public interface SurveyService {
	SurveyVO getSurveyDetails(String surveyId);
	boolean createSurvey(SurveyVO survey);
	List<SurveyVO> getAllSurveys();
	boolean submitResponse(String surveyId, SurveyResponseVO responseVO);
	SurveyResultVO getSurveyResults(String surveyId);
	List<SurveyVO> getClosedSurveys();
	List<SurveyVO> getSurveyListFromApi();
	List<SurveyVO> getSurveyList(String companyNo);
	List<SurveyVO> getClosedSurveyList(String companyNo);
	Map<String, Integer> getSurveyResponseCounts(List<String> surveyIds);
}
