package kr.or.ddit.works.mail.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

import kr.or.ddit.works.mail.service.MailService;
import kr.or.ddit.works.mail.vo.MailUserAuthVO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GMailConfig {

    @Lazy
    private final MailService mailService;

    private static final String APPLICATION_NAME = "SEP-Gmail";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.MAIL_GOOGLE_COM);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private GoogleClientSecrets clientSecrets;
    private HttpTransport httpTransport;

    @PostConstruct
    public void init() throws Exception {
        InputStream in = GMailConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) throw new IllegalArgumentException("credentials.json 못 찾음");

        clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }
    
    public GoogleClientSecrets getClientSecrets() {
        return clientSecrets;
    }
    
    public HttpTransport getHttpTransport() {
        return httpTransport;
    }

    public JsonFactory getJsonFactory() {
        return JSON_FACTORY;
    }

    public List<String> getScopes() {
        return SCOPES;
    }

    public Gmail getGmailService(String userId) throws Exception {
        Credential credential = buildCredential(userId);
        if (credential == null || credential.getAccessToken() == null) {
            throw new IllegalStateException("사용자 인증 필요");
        }

        if (credential.getExpiresInSeconds() != null && credential.getExpiresInSeconds() <= 60) {
            boolean refreshed = credential.refreshToken();
            if (refreshed) {
                updateToken(userId, credential);
            }
        }

        return new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public Credential buildCredential(String userId) throws Exception {
        MailUserAuthVO token = mailService.getToken(userId);
        if (token == null) return null;

        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setClientAuthentication(new ClientParametersAuthentication(
                        clientSecrets.getDetails().getClientId(),
                        clientSecrets.getDetails().getClientSecret()))
                .setTokenServerUrl(new GenericUrl("https://oauth2.googleapis.com/token"))
                .build();

        credential.setAccessToken(token.getAccessToken());
        credential.setRefreshToken(token.getRefreshToken());
        credential.setExpirationTimeMilliseconds(token.getTokenExpiry().getTime());

        return credential;
    }

    public GoogleAuthorizationCodeFlow getAuthorizationFlow() throws Exception {
        return new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
    }

    private void updateToken(String userId, Credential credential) {
        MailUserAuthVO updated = new MailUserAuthVO();
        updated.setEmpId(userId);
        updated.setAccessToken(credential.getAccessToken());
        updated.setRefreshToken(credential.getRefreshToken());
        updated.setTokenExpiry(new Timestamp(credential.getExpirationTimeMilliseconds()));
        mailService.saveOrUpdateToken(updated);
    }
}
