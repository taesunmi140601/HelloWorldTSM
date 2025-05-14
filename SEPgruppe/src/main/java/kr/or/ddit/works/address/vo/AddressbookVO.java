package kr.or.ddit.works.address.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 주소록 ADDRESSBOOK VO
 * @author JYS
 * @since 2025. 3. 15.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 15.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
public class AddressbookVO implements Serializable{
	private Long adbkNo;      		//주소록번호
	private String empId;      		//사원 아이디
	private String adbkName;     	//이름
	private String adbkCompany;     //회사명
	private String adbkDept;      	//부서
	private String adbkPosition;    //직급
	private String adbkEmail;      	//이메일
	private String adbkPhone;      	//휴대폰번호
	private String adbkMemo;      	//메모
	private String adbkDelYn;      	//삭제 여부
	private String adbkIsExternal;  //내부/외부 구분 (I/E)
	private String companyNo;



}
