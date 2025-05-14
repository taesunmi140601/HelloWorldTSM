package kr.or.ddit.works.survey.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
@Service
public class SurveyApiServiceImpl implements SurveyApiService {

    private static final String API_BASE = "https://api.surveymonkey.com/v3";
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 🔐 절대로 프론트엔드에서 노출되지 않도록 서버 내에서만 사용!
    private static final String ACCESS_TOKEN = "LXs5ovMHyr4wSkLSS8XATbigcAhdK2MRdFnrpPtm8HGc0Z7yFQ6pzAMtSeY1swdDNxsOHhfU0xmu7yvo-m5Irkc0s6ZJsoYnhkAc-w22lNM6EcI7mxUD9p0MD-7owmfv";

    @Override
    public String createSurvey(String jsonBody) throws Exception {
        String url = API_BASE + "/surveys";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(jsonBody, "UTF-8"));

            try (CloseableHttpResponse response = client.execute(post)) {
                int status = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (status >= 200 && status < 300) {
                    return responseBody;
                } else {
                    throw new RuntimeException("설문 생성 API 호출 실패: " + status + "\n" + responseBody);
                }
            }
        }
    }

    @Override
    public boolean createSurveyOnApi(SurveyVO surveyVO) {
        // 추후 확장용 - 현재 미사용
        return false;
    }

    @Override
    public SurveyVO getSurveyDetailsFromApi(String surveyId) {
        String url = API_BASE + "/surveys/" + surveyId;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            get.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);

            try (CloseableHttpResponse response = client.execute(get)) {
                int status = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (status >= 200 && status < 300) {
                    // ✅ Jackson을 이용해 JSON 파싱
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);

                    SurveyVO survey = new SurveyVO();
                    survey.setSurveyId(jsonNode.get("id").asText());
                    survey.setTitle(jsonNode.get("title").asText());

                    // ✅ 생성일이 존재하면 추가
                    if (jsonNode.has("date_created")) {
                        survey.setDateCreated(jsonNode.get("date_created").asText());
                    }

                    // ✅ 생성자: 우리 시스템에서 따로 세팅해야 함 (예: 로그인 사용자 ID)
                    survey.setCreator("admin");  // 또는 세션에서 추출

                    return survey;
                } else {
                    throw new RuntimeException("설문 상세 조회 실패: " + status + "\n" + responseBody);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("설문 상세 조회 중 오류 발생", e);
        }
    }

    @Override
    public boolean submitSurveyResponseToApi(String surveyId, SurveyResponseVO responseVO) {
        // 추후 확장용 - 현재 미사용
        return false;
    }
    @Override
    public List<SurveyVO> getAllSurveys() {
        String url = API_BASE + "/surveys";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            get.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);

            try (CloseableHttpResponse response = client.execute(get)) {
                int status = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (status >= 200 && status < 300) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode root = objectMapper.readTree(responseBody);
                    JsonNode data = root.get("data");

                    List<SurveyVO> surveyList = new ArrayList<>();
                    for (JsonNode item : data) {
                        SurveyVO survey = new SurveyVO();
                        survey.setSurveyId(item.get("id").asText());
                        survey.setTitle(item.get("title").asText());

                        if (item.has("date_created")) {
                            survey.setDateCreated(item.get("date_created").asText());
                        }

                        survey.setCreator("admin");

                        surveyList.add(survey);
                    }

                    return surveyList;
                } else {
                    throw new RuntimeException("설문 목록 조회 실패: " + status + "\n" + responseBody);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("설문 목록 조회 중 오류 발생", e);
        }
    }
    
    @Override
    public Map<String, Integer> getSurveyResponseCounts(List<String> surveyIds) {
        Map<String, Integer> responseCounts = new HashMap<>();

        for (String surveyId : surveyIds) {
            String url = API_BASE + "/surveys/" + surveyId + "/responses/bulk?page=1&per_page=1";

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet get = new HttpGet(url);
                get.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
                get.setHeader("Content-Type", "application/json");

                try (CloseableHttpResponse response = client.execute(get)) {
                    String json = EntityUtils.toString(response.getEntity(), "UTF-8");

                    // ✅ Jackson으로 파싱
                    JsonNode root = objectMapper.readTree(json);
                    int total = root.path("total").asInt(0);  // 없으면 기본 0

                    responseCounts.put(surveyId, total);
                }
            } catch (Exception e) {
                responseCounts.put(surveyId, 0); // 실패 시 0
                e.printStackTrace();
            }
        }

        return responseCounts;
    }
}
