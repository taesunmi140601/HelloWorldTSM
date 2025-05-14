package kr.or.ddit.works.approval.enums;

public enum AprvlLineStatus {

	Waiting("W","대기중"),
	Approved("A","승인"),
	Rejected("R","반려"),
	Hold("H","보류"),
	Canceled("X", "취소"),
	Draft("D","기안"),
	Reference("F","참조");


	private final String code;
    private final String label;
    
    AprvlLineStatus(String code, String label) {
    	this.code = code;
        this.label = label;
	}
    
    public String getCode() {
    	return code;
    }
    
    public String getLabel() {
    	return label;
    }
    
    public static AprvlLineStatus fromCode(String code) {
        for (AprvlLineStatus status : AprvlLineStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
