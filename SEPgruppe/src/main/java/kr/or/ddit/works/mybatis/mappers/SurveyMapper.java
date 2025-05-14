package kr.or.ddit.works.mybatis.mappers;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.survey.vo.SurveyQuestionsVO;
import kr.or.ddit.works.survey.vo.SurveyResponseVO;
import kr.or.ddit.works.survey.vo.SurveyVO;

/**
 * 설문 매퍼
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
@Mapper
public interface SurveyMapper {
	// 설문 등록
    int insertSurvey(SurveyVO survey);

    // 질문 등록
    int insertQuestion(SurveyQuestionsVO question);

    // 선택지 등록
    int insertChoice(@Param("questionId") Long questionId, @Param("choiceText") String choiceText);

    // 설문 전체 조회 (진행 중)
    List<SurveyVO> selectAllSurveys();

    // 마감 설문 조회
    List<SurveyVO> selectClosedSurveys();

    // 설문 상세 조회
    SurveyVO selectSurveyById(String surveyId);

    // 설문 응답 저장
    int insertResponse(SurveyResponseVO.QuestionResponse response);

    // 결과 집계용 데이터
    List<Map<String, Object>> selectResultStats(String surveyId);
}