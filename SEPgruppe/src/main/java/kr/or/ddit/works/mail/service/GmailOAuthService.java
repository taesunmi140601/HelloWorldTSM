package kr.or.ddit.works.mail.service;

public interface GmailOAuthService {

	public String getAuthorizationUrl(String empId);
	
	public void processOAuthCallback(String code, String state);
	
}
