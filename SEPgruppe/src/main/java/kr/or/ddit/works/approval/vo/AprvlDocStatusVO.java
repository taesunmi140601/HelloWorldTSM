package kr.or.ddit.works.approval.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class AprvlDocStatusVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String statusCd;      //문서 상태 코드
	private String statusName;      //문서 상태 이름
	private String statusDesc;      //문서 상태 설명

}
