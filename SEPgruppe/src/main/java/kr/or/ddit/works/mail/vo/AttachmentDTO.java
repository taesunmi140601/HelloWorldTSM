package kr.or.ddit.works.mail.vo;

import lombok.Data;

@Data
public class AttachmentDTO {
	private String attachmentId;
    private String filename;
    private String mimeType;
    private String data;
}
