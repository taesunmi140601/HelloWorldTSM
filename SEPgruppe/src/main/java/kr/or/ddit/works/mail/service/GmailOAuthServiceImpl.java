package kr.or.ddit.works.mail.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.GmailScopes;

import kr.or.ddit.works.mail.config.GMailConfig;
import kr.or.ddit.works.mail.vo.MailUserAuthVO;
import kr.or.ddit.works.mybatis.mappers.MailMapper;

@Service
public class GmailOAuthServiceImpl implements GmailOAuthService {

    @Inject
    private MailMapper mailMapper;

    @Inject
    private GMailConfig gmailConfig;

    private String clientId;
    private String clientSecret;
    private final String redirectUri = "https://localhost/sep/mail/oauth/callback";
    private final java.util.List<String> scopes = Arrays.asList(GmailScopes.MAIL_GOOGLE_COM);

    @PostConstruct
    public void init() {
        this.clientId = gmailConfig.getClientSecrets().getDetails().getClientId();
        this.clientSecret = gmailConfig.getClientSecrets().getDetails().getClientSecret();
    }

    @Override
    public String getAuthorizationUrl(String empId) {
        String state = UUID.randomUUID().toString() + "|" + empId;
        try {
            return new GoogleAuthorizationCodeRequestUrl(
                    clientId,
                    redirectUri,
                    scopes
            )
            .setAccessType("offline")
            .setApprovalPrompt("force")
            .setState(state)
            .build();
        } catch (Exception e) {
            throw new RuntimeException("Authorization URL 생성 실패", e);
        }
    }

    @Override
    public void processOAuthCallback(String code, String state) {
        String empId = extractEmpId(state);
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    clientId,
                    clientSecret,
                    code,
                    redirectUri
            ).execute();

            MailUserAuthVO token = new MailUserAuthVO();
            token.setEmpId(empId);
            token.setAccessToken(tokenResponse.getAccessToken());
            token.setRefreshToken(tokenResponse.getRefreshToken());
            token.setTokenExpiry(new Timestamp(System.currentTimeMillis() + (tokenResponse.getExpiresInSeconds() * 1000L)));

            MailUserAuthVO existing = mailMapper.selectTokenByUserId(empId);
            if (existing == null) {
                mailMapper.insertToken(token);
            } else {
                mailMapper.updateToken(token);
            }

        } catch (Exception e) {
            throw new RuntimeException("OAuth 콜백 처리 실패", e);
        }
    }

    private String extractEmpId(String state) {
        String[] parts = state.split("\\|");
        if (parts.length > 1) {
            return parts[1];
        }
        throw new RuntimeException("state에 empId 없음");
    }
}
