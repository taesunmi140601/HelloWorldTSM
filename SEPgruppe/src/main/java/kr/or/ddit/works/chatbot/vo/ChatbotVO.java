package kr.or.ddit.works.chatbot.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 챗봇 CHATBOT VO
 * @author JYS
 * @since 2025. 3. 16.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 16.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
public class ChatbotVO implements Serializable{
	
	private Long qaId;      			//질문-답변 세트의 고유ID
	private String companyNo;      		//고객사번호
	private String userQuestion;      	//사용자가 입력할 예상 질문
	private String chatbotResponse;     //챗봇이 응답할 답변
	private String chatbotCreatedAt;    //생성일
	private String chatbotUpdatedAt;    //수정일

}
