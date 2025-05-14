package kr.or.ddit;

import org.springframework.security.core.Authentication;

public class FetchEvent {
	private final Authentication authentication;

	public FetchEvent(Authentication authentication) {
		this.authentication = authentication;
	}

	public Authentication getAuthentication() {
		return authentication;
	}
}
