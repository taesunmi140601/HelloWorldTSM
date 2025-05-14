package kr.or.ddit.works.company.vo;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.validate.InsertGroup;
import kr.or.ddit.works.consultation.vo.ConsultationRequestsVO;
import kr.or.ddit.works.login.vo.AllUserVO;
import kr.or.ddit.works.subscription.vo.PaymentsVO;
import kr.or.ddit.works.subscription.vo.SubscriptionRequestsVO;
import kr.or.ddit.works.subscription.vo.SubscriptionsVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author JYS
 * @since 2025. 3. 14.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------- 	-----------
 *  2025. 3. 14.     	JYS	          최초 생성
 *
 * </pre>
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of="contactId")
@Data
public class CompaniesVO extends AllUserVO implements Serializable{

	@NotBlank(groups = InsertGroup.class)
	private String contactId;
	private String companyName;
	@NotBlank(groups = InsertGroup.class)
	private String contactNm;
	@NotBlank
	@Size(min = 13,max = 13)
	private String contactPhone;
	@NotBlank
	@Email
	private String contactEmail;
	@NotBlank
	private String companyZip;
	private String companyAdd1;
	private String companyAdd2;
	@NotBlank
	@Size(min = 4,max = 20)
	private String contactPw;
	@Size(min = 10, max = 10)
	private String businessRegNo;
	private String isDeleted;
	private String companyNo;
	private String adminId;
	private String signUp;



	private List<SubscriptionsVO> subscriptions;
	private List<PaymentsVO> payment;
	private List<ConsultationRequestsVO> consultationRequests;
	private List<SubscriptionRequestsVO> subscriptionRequests;

}
