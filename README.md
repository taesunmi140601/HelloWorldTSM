# 태선미 포트폴리오

## 🖥️ 프로젝트 소개
클라우드 기반의 사내 그룹웨어 서비스 
<br>

## 🕰️ 개발 기간
* 25.02.25일 - 25.04.17일 (6명)

### ⚙️ 개발 환경
- `Java 17`
- **IDE** : STS 4.28
- **Framework** : Spring(5.3.39)
- **Database** : Oracle DB(23c)
- **ORM** : Mybatis
- **BootStrap** : 5.3.3

### 📡 개발 환경 (API)
- Google Drive API : 웹하드 작업
- Google Maps API : 고객사 위치 표시
- GMail API : 메일 알림
- ServeyMonkey API : 설문조사
- CK Editor : 심화된 텍스트 편집 기능
- fullcalendar API : 예약 및 스케줄표
- Tidio API : Rule-Based 기반 챗봇
- OAuth 2.0 : API 인증 및 권한 관리

## 📌 담당 기능
- 📦 BootStrap 템플릿 적용 및 페이지 레이아웃 분리  
  - 🎨 그룹웨어 공통 레이아웃 및 템플릿 구성 (Tiles 설정) [🔗 바로가기](https://github.com/taesunmi140601/HelloWorldTSM/tree/main/SEPgruppe/src/main/webapp/WEB-INF/tiles)
  - 📄 그룹웨어 화면 공통 include 파일 관리 [🔗 바로가기](https://github.com/taesunmi140601/HelloWorldTSM/tree/main/SEPgruppe/src/main/webapp/WEB-INF/views/groupware/includee)
  - 📑 그룹웨어 전용 레이아웃 JSP 관리 [🔗 바로가기](https://github.com/taesunmi140601/HelloWorldTSM/tree/main/SEPgruppe/src/main/webapp/WEB-INF/views/groupware/layouts)
  - 📄 서비스제공자 화면 공통 include 파일 관리 [🔗 바로가기](https://github.com/taesunmi140601/HelloWorldTSM/tree/main/SEPgruppe/src/main/webapp/WEB-INF/views/sepgruppe/includee)
  - 📑 서비스제공자 전용 레이아웃 JSP 관리 [🔗 바로가기](https://github.com/taesunmi140601/HelloWorldTSM/tree/main/SEPgruppe/src/main/webapp/WEB-INF/views/sepgruppe/layouts)

- 📦 첨부파일 업로드 & 다운로드 공통 모듈 개발 [🔗 바로가기](https://github.com/taesunmi140601/HelloWorldTSM/tree/main/SEPgruppe/src/main/java/kr/or/ddit/works/attachFile)

<details>
<summary>📦 채팅 기능 개발 (WebSocketConfig 설정)</summary>

```java
package kr.or.ddit.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSocketMessageBroker
@Configuration
@Controller
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // STOMP 엔드포인트 등록 및 SockJS 지원 활성화
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp")
                .setAllowedOrigins("/wss")
                .withSockJS();
    }
    
    // 메시지 브로커 관련 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

}
### 📂 관련 소스 경로
- 📄 📦 채팅 기능 컨트롤러 ([ChatController](https://github.com/taesunmi140601/HelloWorldTSM/tree/main/SEPgruppe/src/main/java/kr/or/ddit/works/chat/controller))
- 📄 🛠️ 채팅 서비스 로직 ([ChatService](https://github.com/taesunmi140601/HelloWorldTSM/tree/main/SEPgruppe/src/main/java/kr/or/ddit/works/chat/service))
- 📄 📑 채팅 데이터 VO ([ChatVO](https://github.com/taesunmi140601/HelloWorldTSM/tree/main/SEPgruppe/src/main/java/kr/or/ddit/works/chat/vo))
- 📄 🗂️ 채팅 MyBatis 매퍼 ([ChatMapper.xml](https://github.com/taesunmi140601/HelloWorldTSM/blob/main/SEPgruppe/src/main/java/kr/or/ddit/works/mybatis/mappers/ChatMapper.java))
- 📄 🖥️ 채팅 화면 JSP ([chat](https://github.com/taesunmi140601/HelloWorldTSM/tree/main/SEPgruppe/src/main/webapp/WEB-INF/views/groupware/chat))
- 📄 📜 채팅 JS 파일 ([chat](https://github.com/taesunmi140601/HelloWorldTSM/tree/main/SEPgruppe/src/main/webapp/resources/groupware/js/chat))

</details>



