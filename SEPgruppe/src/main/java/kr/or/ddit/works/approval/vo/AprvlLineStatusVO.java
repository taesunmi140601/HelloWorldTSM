package kr.or.ddit.works.approval.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class AprvlLineStatusVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String lineStatusCd;      //결재선 상태 코드
	private String lineStatusName;      //결재선 상태 이름
	private String lineStatusDesc;      //결재선 상태 설명
}
