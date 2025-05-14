package kr.or.ddit.works.approval.enums;

public enum AprvlDocStatus {
    TEMP_SAVE("TEMP_SAVE", "임시저장", "#6c757d"),     // 회색
    SUBMITTED("SUBMITTED", "상신완료", "#0d6efd"),    // 파랑
    IN_PROGRESS("IN_PROGRESS", "결재중", "#ffc107"),  // 노랑
    APPROVED("APPROVED", "결재완료", "#198754"),      // 초록
    REJECTED("REJECTED", "반려", "#dc3545"),          // 빨강
    WITHDRAWN("WITHDRAWN", "회수", "#fd7e14"),         // 주황
    ON_HOLD("ON_HOLD", "보류", "#6f42c1"),            // 보라
    DELETED("DELETED", "삭제", "#343a40");            // 어두운 회색

    private final String code;
    private final String label;
    private final String color;

    AprvlDocStatus(String code, String label, String color) {
        this.code = code;
        this.label = label;
        this.color = color;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public String getColor() {
        return color;
    }

    public static AprvlDocStatus fromCode(String code) {
        for (AprvlDocStatus status : AprvlDocStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}


