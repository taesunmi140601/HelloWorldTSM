package kr.or.ddit.works.alarm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OneSignalService {
    // OneSignal에서 발급받은 API Key와 App ID (실제 값으로 대체하세요)
    private static final String REST_API_KEY = "os_v2_app_imj64gq7g5cilalea5c26slaka7umu4mn3cuc2epwznxyfhdii6f2kphmdupygrjtpjsdqzfhbebpqmxbcg2g43lbplp3tyzdwxwtmq";
    private static final String APP_ID = "4313ee1a-1f37-4485-8164-0745af496050";
    private static final String ONESIGNAL_API_URL = "https://onesignal.com/api/v1/notifications";

    /**
     * 지정된 메시지를 선택된 사용자(playerIds)에게 전송합니다.
     *
     * @param message   전송할 알림 메시지
     * @param playerIds 알림을 수신할 사용자의 OneSignal Player ID 목록
     */
    public void sendNotification(String message, List<String> playerIds) {
        try {
            // ObjectMapper를 이용하여 JSON 변환을 위한 Map 객체 생성
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = new HashMap<>();
            payload.put("app_id", APP_ID);

            // 알림 내용 설정 (영어 메시지를 예시로 함)
            Map<String, String> contents = new HashMap<>();
            contents.put("en", message);
            payload.put("contents", contents);

            // 대상 사용자들의 OneSignal Player ID 목록 설정
            payload.put("include_player_ids", playerIds);

            // Map을 JSON 문자열로 변환
            String jsonPayload = mapper.writeValueAsString(payload);

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + REST_API_KEY);

            // HttpEntity에 헤더와 JSON 데이터를 담아서 생성
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

            // RestTemplate을 이용해 POST 요청 전송
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(ONESIGNAL_API_URL, entity, String.class);

            // 응답 코드 및 본문 출력
            System.out.println("Response Code: " + response.getStatusCodeValue());
            System.out.println("Response Body: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
