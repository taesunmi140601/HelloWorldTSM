package kr.or.ddit.works.subscription.service;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import kr.or.ddit.works.mybatis.mappers.PaymentMapper;
import kr.or.ddit.works.subscription.vo.BillingKeyVO;
import kr.or.ddit.works.subscription.vo.PaymentsVO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 포트원(아임포트) 정기결제 관련 로직을 담당하는 Service
 * Jackson 사용 + OkHttp로 API 호출 예시
 */
@Service
public class PaymentServiceImpl implements PaymentService {

	@Inject
	PaymentMapper mapper;
	
    // 포트원에서 발급받은 REST API 키 & 시크릿
    private static final String REST_API_KEY = "1386521470850878";
    private static final String REST_API_SECRET = "SNiMzyAB2Kp3yBDrsO7Cw8MrdAm7yrlVoXExSmZaGb8IryYw0Oe2ppy7bvoZ3OxLWmXYbsmCNXPEBQFv";

    // 포트원 서버 주소
    private static final String IAMPORT_API_URL = "https://api.iamport.kr";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * (1) 액세스 토큰 발급
     * POST /users/getToken
     */
    public String getAccessToken() throws IOException {
        // 요청 바디
        JsonNode requestBodyJson = objectMapper.createObjectNode()
            .put("imp_key", REST_API_KEY)
            .put("imp_secret", REST_API_SECRET);

        RequestBody requestBody = RequestBody.create(
                requestBodyJson.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
            .url(IAMPORT_API_URL + "/users/getToken")
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("토큰 발급 실패: " + response);
            }
            String body = response.body().string();
            JsonNode json = objectMapper.readTree(body);

            // code == 0 이면 성공
            if (json.get("code").asInt() != 0) {
                throw new IOException("토큰 발급 실패: " + json.get("message").asText());
            }

            // response.access_token
            return json.get("response").get("access_token").asText();
        }
    }


    public JsonNode requestSchedulePayment(
    	String customerUid, String merchantUid, long scheduleTimestamp
    	, long amount, String planType, PaymentsVO payment
    ) throws IOException {
        // 1) 토큰 발급
        String accessToken = getAccessToken();

        // 2) JSON 생성
        ObjectMapper objectMapper = new ObjectMapper();
        
        
        ObjectNode schedule1 = objectMapper.createObjectNode();
        schedule1.put("merchant_uid", merchantUid);
        schedule1.put("schedule_at", scheduleTimestamp);
        schedule1.put("amount", amount);
        schedule1.put("name", planType+"플랜 정기결제");

        ArrayNode schedules = objectMapper.createArrayNode();
        schedules.add(schedule1);

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("customer_uid", customerUid);
        requestBody.set("schedules", schedules);

        // 3) HTTP 요청 (OkHttp 예시)
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url("https://api.iamport.kr/subscribe/payments/schedule")
                .addHeader("Authorization", accessToken)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            mapper.insertPayment(payment);
            return objectMapper.readTree(responseBody); // { code:0, message:"", response:{...} }
        }
    }


	@Override
	public void saveBilling(BillingKeyVO billingKey) {
		mapper.saveBilling(billingKey);
	}


	@Override
	public BillingKeyVO selectBilling(String contactId) {
		return mapper.selectBilling(contactId);
	}


	@Override
	public List<PaymentsVO> paymentList() {
		return mapper.paymentList();
	}


	@Override
	public List<PaymentsVO> selectPaymentsBySubscriptionNo(Long subscriptionNo) {
		return mapper.selectPaymentsBySubscriptionNo(subscriptionNo);
	}
}
