package kr.or.ddit.works.provider.vo;

import java.io.Serializable;

import kr.or.ddit.works.login.vo.AllUserVO;
import lombok.Data;
import lombok.ToString;

/**
 * 관리자 VO 
 * @author JYS
 * @since 2025. 3. 14.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 14.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
@ToString(callSuper = true)
public class ProviderVO extends AllUserVO implements Serializable{
	
	private String providerId;      	//서비스 제공자 아이디
	private String providerPw;      	//서비스 제공자 비밀번호
	private String providerNm;      	//서비스 제공자 이름
	private String providerTel;      	//서비스 제공자 연락처
	private String providerRetire;      //사용 여부
}
