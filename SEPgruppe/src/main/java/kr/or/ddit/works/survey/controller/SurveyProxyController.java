package kr.or.ddit.works.survey.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.works.survey.api.SurveyApiService;
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
@Controller
@RequestMapping("/{companyNo}/surveyApi")
public class SurveyProxyController {
	
	@Autowired
    private SurveyApiService surveyApiService;

    private static final String ACCESS_TOKEN = "LXs5ovMHyr4wSkLSS8XATbigcAhdK2MRdFnrpPtm8HGc0Z7yFQ6pzAMtSeY1swdDNxsOHhfU0xmu7yvo-m5Irkc0s6ZJsoYnhkAc-w22lNM6EcI7mxUD9p0MD-7owmfv";
    private static final String SURVEY_MONKEY_URL = "https://api.surveymonkey.com/v3/surveys";

    // ✅ 설문 목록 조회
    @GetMapping("/surveys")
    public ResponseEntity<String> getSurveys() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(SURVEY_MONKEY_URL);
            get.setHeader(	"Authorization", "Bearer " + ACCESS_TOKEN);
            get.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = client.execute(get)) {
                int status = response.getStatusLine().getStatusCode();
                String body = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (status >= 200 && status < 300) {
                    return ResponseEntity.ok(body);
                } else {
                    return ResponseEntity.status(status).body("API 오류: " + body);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
        }
    }
 
    // ✅ 설문 목록 JSON 조회 (JS에서 fetch용)
    @GetMapping("/list")
    public String getSurveyListFromApi(Model model, @PathVariable("companyNo") String companyNo) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(SURVEY_MONKEY_URL);
            get.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            get.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = client.execute(get)) {
                String body = EntityUtils.toString(response.getEntity(), "UTF-8");

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(body);
                List<SurveyVO> surveyList = new ArrayList<>();

                for (JsonNode item : root.path("data")) {
                    SurveyVO survey = new SurveyVO();
                    String surveyId = item.get("id").asText();
                    survey.setSurveyId(surveyId);
                    survey.setTitle(item.get("title").asText());

                    // ✅ closed 상태
                    if (item.has("status")) {
                        String status = item.get("status").asText();
                        survey.setClosed("closed".equalsIgnoreCase(status) ? "Y" : "N");
                    }

                    // ✅ 생성자
                    survey.setCreator("admin");

                    // ✅ 상세 조회로 생성일 보완
                    try {
                        SurveyVO detailed = surveyApiService.getSurveyDetailsFromApi(surveyId);
                        survey.setDateCreated(detailed.getDateCreated());
                    } catch (Exception e) {
                        System.err.println("⚠️ 설문 상세 조회 실패: " + surveyId);
                        survey.setDateCreated("조회 실패");
                    }

                    surveyList.add(survey);
                }

                // ✅ 응답 수 조회 추가
                List<String> surveyIds = surveyList.stream()
                    .map(SurveyVO::getSurveyId)
                    .toList(); // Java 16+ 또는 Collectors.toList()도 가능

                Map<String, Integer> responseCounts = surveyApiService.getSurveyResponseCounts(surveyIds);
                model.addAttribute("responseCounts", responseCounts);

                // ✅ 설문 분류
                List<SurveyVO> recentSurveyList = new ArrayList<>();
                List<SurveyVO> closedSurveyList = new ArrayList<>();

                for (SurveyVO survey : surveyList) {
                    if ("Y".equalsIgnoreCase(survey.getClosed())) {
                        closedSurveyList.add(survey);
                    } else {
                        recentSurveyList.add(survey);
                    }
                }

                // ✅ 모델에 전달
                model.addAttribute("companyNo", companyNo);
                model.addAttribute("recentSurveyList", recentSurveyList);
                model.addAttribute("closedSurveyList", closedSurveyList);

                return "gw:survey/surveyList";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "SurveyMonkey API 조회 실패: " + e.getMessage());
            return "error";
        }
    }

    // ✅ 설문 생성 (surveyCreate)
    @PostMapping("/surveyCreate")
    public ResponseEntity<?> surveyCreate(Model model,@RequestBody String jsonBody, @PathVariable("companyNo") String companyNo) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(SURVEY_MONKEY_URL);
            post.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(jsonBody, "UTF-8"));

            try (CloseableHttpResponse response = client.execute(post)) {
                int status = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (status >= 200 && status < 300) {
                    return ResponseEntity.ok(responseBody);
                } else {
                    return ResponseEntity.status(status)
                            .body(Collections.singletonMap("error", "API 오류: " + responseBody));
                }
            }
        } catch (Exception e) {
        	model.addAttribute("companyNo", companyNo);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    
    // ✅ 설문 상세 페이지 (survey.jsp용)
    @GetMapping("/surveys/{surveyId}/details")
    public String getSurveyDetailsPage(
            @PathVariable String companyNo,
            @PathVariable String surveyId,
            Model model) {

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet("https://api.surveymonkey.com/v3/surveys/" + surveyId + "/details");
            get.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            get.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = client.execute(get)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (statusCode >= 200 && statusCode < 300) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode json = mapper.readTree(responseBody);

                    // 🎯 JSON을 문자열로 model에 전달 (JSP에서 JSON.parse 해서 쓰기 좋음)
                    model.addAttribute("surveyJson", json.toString());
                    model.addAttribute("surveyId", surveyId);
                    model.addAttribute("companyNo", companyNo);

                    return "gw:survey/survey";  // Tiles 타일즈 뷰 이름
                } else {
                    model.addAttribute("error", "설문 조회 실패");
                    return "error";
                }
            }
        } catch (Exception e) {
            model.addAttribute("error", "서버 오류: " + e.getMessage());
            return "error";
        }
    }

    // ✅ 설문 상세 정보(JSON)
    @GetMapping("/surveys/{surveyId}/details/data")
    @ResponseBody
    public ResponseEntity<?> getSurveyDetailsData(
            @PathVariable String companyNo,
            @PathVariable String surveyId) {

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet("https://api.surveymonkey.com/v3/surveys/" + surveyId + "/details");
            get.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            get.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = client.execute(get)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (statusCode >= 200 && statusCode < 300) {
                    return ResponseEntity.ok(responseBody); // 🔥 JSON 그대로 반환
                } else {
                    return ResponseEntity.status(statusCode).body("API 호출 실패");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }
    
    // ✅ Collector 조회/생성
    @GetMapping("/collectors/{surveyId}")
    @ResponseBody
    public ResponseEntity<?> getOrCreateCollector(
            @PathVariable String companyNo,
            @PathVariable String surveyId) {

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // 기존 Collector 조회
            HttpGet get = new HttpGet("https://api.surveymonkey.com/v3/surveys/" + surveyId + "/collectors");
            get.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            get.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = client.execute(get)) {
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                JsonNode jsonNode = new ObjectMapper().readTree(responseBody);

                if (jsonNode.has("data") && jsonNode.get("data").size() > 0) {
                    String collectorId = jsonNode.get("data").get(0).get("id").asText();
                    return ResponseEntity.ok(Collections.singletonMap("collector_id", collectorId));
                }
            }

            // 없으면 새로 생성
            HttpPost post = new HttpPost("https://api.surveymonkey.com/v3/surveys/" + surveyId + "/collectors");
            post.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity("{\"type\": \"weblink\", \"name\": \"Web Collector\"}", "UTF-8"));

            try (CloseableHttpResponse response = client.execute(post)) {
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                JsonNode jsonNode = new ObjectMapper().readTree(responseBody);

                if (jsonNode.has("id")) {
                    String collectorId = jsonNode.get("id").asText();
                    return ResponseEntity.ok(Collections.singletonMap("collector_id", collectorId));
                } else {
                    return ResponseEntity.status(500).body("Collector 생성 실패");
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    // ✅ 응답 제출
    @PostMapping("/collectors/{collectorId}/responses")
    @ResponseBody
    public ResponseEntity<?> submitSurveyResponse(
            @PathVariable String companyNo,
            @PathVariable String collectorId,
            @RequestBody String requestBody) {

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("https://api.surveymonkey.com/v3/collectors/" + collectorId + "/responses");
            post.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(requestBody, "UTF-8"));

            try (CloseableHttpResponse response = client.execute(post)) {
                int status = response.getStatusLine().getStatusCode();
                String responseData = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (status >= 200 && status < 300) {
                    return ResponseEntity.ok(responseData);
                } else {
                    return ResponseEntity.status(status).body("SurveyMonkey 응답 제출 실패: " + responseData);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류: " + e.getMessage());
        }
    }
    
    // ✅ 설문 응답 통계 조회 
    @GetMapping("/surveys/{surveyId}/responses")
    @ResponseBody
    public ResponseEntity<?> getSurveyResponses(
            @PathVariable String companyNo,
            @PathVariable String surveyId) {

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet("https://api.surveymonkey.com/v3/surveys/" + surveyId + "/responses/bulk");
            get.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            get.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = client.execute(get)) {
                int status = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (status >= 200 && status < 300) {
                    return ResponseEntity.ok(responseBody);
                } else {
                    return ResponseEntity.status(status).body("응답 통계 API 호출 실패: " + responseBody);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    // ✅ 설문 결과 뷰 렌더링 (surveyResult.jsp 보여줌)
    @GetMapping("/surveys/{surveyId}/results")
    public String showSurveyResultPage(
            @PathVariable String companyNo,
            @PathVariable String surveyId,
            Model model) {
        
        model.addAttribute("companyNo", companyNo);
        model.addAttribute("surveyId", surveyId);
        return "gw:survey/surveyResult"; // 타일즈 안 쓰면 그냥 "survey/surveyResult"
    }
}
