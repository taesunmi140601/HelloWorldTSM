package kr.or.ddit.works.mail.exception;

public class NeedOAuthRedirectException extends RuntimeException {

    public NeedOAuthRedirectException(String message) {
        super(message);
    }

    public NeedOAuthRedirectException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeedOAuthRedirectException() {
        super("Gmail 인증이 필요합니다.");
    }
}
