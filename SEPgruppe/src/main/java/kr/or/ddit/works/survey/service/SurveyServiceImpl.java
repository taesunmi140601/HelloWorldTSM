package kr.or.ddit.works.survey.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.mybatis.mappers.SurveyMapper;
import kr.or.ddit.works.survey.api.SurveyApiService;
import kr.or.ddit.works.survey.vo.SurveyResponseVO;
import kr.or.ddit.works.survey.vo.SurveyResultVO;
import kr.or.ddit.works.survey.vo.SurveyVO;

/**
 * 설문 서비스 임플
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
@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyApiService surveyApiService;
    private SurveyMapper surveyMapper;

    @Override
    public boolean createSurvey(SurveyVO surveyVO) {
        return surveyApiService.createSurveyOnApi(surveyVO);
    }

    @Override
    public SurveyVO getSurveyDetails(String surveyId) {
        return surveyApiService.getSurveyDetailsFromApi(surveyId);
    }

    @Override
    public List<SurveyVO> getAllSurveys() {
        // API 연동 방식으로 대체 필요 (예: 전체 설문 리스트 API 호출)
        throw new UnsupportedOperationException("Survey API 목록 조회는 아직 구현되지 않았습니다.");
    }

    @Override
    public List<SurveyVO> getClosedSurveys() {
        return surveyMapper.selectClosedSurveys();
    }

    @Override
    public boolean submitResponse(String surveyId, SurveyResponseVO responseVO) {
        return surveyApiService.submitSurveyResponseToApi(surveyId, responseVO);
    }

    @Override
    public SurveyResultVO getSurveyResults(String surveyId) {
        // API 결과 통계 조회 연동 필요 시 구현
        throw new UnsupportedOperationException("Survey API 결과 조회는 아직 구현되지 않았습니다.");
    }

	@Override
	public List<SurveyVO> getSurveyListFromApi() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SurveyVO> getSurveyList(String companyNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SurveyVO> getClosedSurveyList(String companyNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> getSurveyResponseCounts(List<String> surveyIds) {
		// TODO Auto-generated method stub
		return null;
	}
}