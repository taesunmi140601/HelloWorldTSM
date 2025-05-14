package kr.or.ddit.works.survey.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.works.survey.service.SurveyService;
import kr.or.ddit.works.survey.vo.SurveyResponseVO;
import kr.or.ddit.works.survey.vo.SurveyResultVO;
import kr.or.ddit.works.survey.vo.SurveyVO;

/**
 * 설문 컨트롤러
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
@Controller
@RequestMapping("/{companyNo}/survey")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    // ✅ 설문 목록 조회 (ProxyService 결과 기반)
    @GetMapping("")
    public String showSurveyList(@PathVariable String companyNo, Model model) {
        List<SurveyVO> recentSurveyList = surveyService.getSurveyListFromApi();
        List<SurveyVO> closedSurveyList = surveyService.getClosedSurveys();

        // 설문 ID 수집
        List<String> surveyIds = new ArrayList<>();
        for (SurveyVO survey : recentSurveyList) {
            surveyIds.add(survey.getSurveyId());
        }
        for (SurveyVO survey : closedSurveyList) {
            surveyIds.add(survey.getSurveyId());
        }

        // 응답 수 조회
        Map<String, Integer> responseCounts = surveyService.getSurveyResponseCounts(surveyIds);

        model.addAttribute("recentSurveyList", recentSurveyList);
        model.addAttribute("closedSurveyList", closedSurveyList);
        model.addAttribute("responseCounts", responseCounts);
        model.addAttribute("companyNo", companyNo);
        return "gw:survey/surveyList";
    }

    // ✅ 설문 생성 폼 이동
    @GetMapping("/create")
    public String showCreateForm(@PathVariable String companyNo, Model model) {
        model.addAttribute("companyNo", companyNo);
        return "gw:survey/surveyCreate";
    }

    // ✅ JSP 폼 기반 설문 생성 후 리디렉션 (설문 생성은 Proxy API 호출을 통해 처리)
    @PostMapping("/survey/create")
    public String createSurveyForm(@ModelAttribute SurveyVO surveyVO, RedirectAttributes redirectAttributes) {
        boolean success = surveyService.createSurvey(surveyVO);
        redirectAttributes.addFlashAttribute("message", success ? "설문이 생성되었습니다!" : "설문 생성 실패!");
        return "redirect:/survey/home";
    }

    // ✅ 설문 응답 제출 (폼 기반)
    @PostMapping("/survey/{surveyId}/submit")
    public String submitSurveyForm(
            @PathVariable String surveyId,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        SurveyResponseVO responseVO = new SurveyResponseVO();
        responseVO.setSurveyId(surveyId);
        List<SurveyResponseVO.QuestionResponse> responses = new ArrayList<>();

        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String param = paramNames.nextElement();
            if (param.startsWith("q_")) {
                String questionId = param.substring(2);
                String[] answers = request.getParameterValues(param);
                if (answers != null) {
                    responses.add(new SurveyResponseVO.QuestionResponse(questionId, Arrays.asList(answers)));
                }
            }
        }
        responseVO.setQuestions(responses);
        surveyService.submitResponse(surveyId, responseVO);

        redirectAttributes.addFlashAttribute("message", "응답이 제출되었습니다!");
        return "redirect:/survey/home";
    }
    
    // ✅ 설문 상세 페이지(survey.jsp)로 이동
    @GetMapping("/view/{surveyId}")
    public String showSurveyView(@PathVariable String companyNo,
                                 @PathVariable String surveyId,
                                 Model model) {
        model.addAttribute("surveyId", surveyId);
        model.addAttribute("companyNo", companyNo);
        return "gw:survey/survey"; 
    }

    // ✅ 결과 보기 페이지 이동 (통계는 Proxy API 기반)
    @GetMapping("/survey/{surveyId}/result")
    public String showSurveyResult(@PathVariable String surveyId, Model model) {
        SurveyResultVO result = surveyService.getSurveyResults(surveyId);
        model.addAttribute("result", result);
        return "gw:survey/surveyResult";
    }
}

















