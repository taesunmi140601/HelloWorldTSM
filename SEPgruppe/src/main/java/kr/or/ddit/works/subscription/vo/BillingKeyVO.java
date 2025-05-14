package kr.or.ddit.works.subscription.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class BillingKeyVO implements Serializable {
	
	private long billingKeyId;
	private String contactId;
	private String billingKey;
	private String createAt;
	private String updateAt;

}
