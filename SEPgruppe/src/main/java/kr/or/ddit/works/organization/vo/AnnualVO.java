package kr.or.ddit.works.organization.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class AnnualVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String leaveTypeCd;      //휴가종류코드
	private String leaveType;      //휴가종류

}
