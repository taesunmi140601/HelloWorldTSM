package kr.or.ddit.works.webhard.vo;

import com.google.api.client.util.DateTime;

import lombok.Data;

@Data
public class GoogleDriveFileVO {
    private String id;
    private String name;
    private DateTime createdTime; 
    private String fileExtension;
    private long size;
    private String webViewLink;

    public GoogleDriveFileVO(String id, String name, DateTime createdTime, String fileExtension, long size, String webViewLink) {
        this.id = id;
        this.name = name;
        this.createdTime = createdTime;
        this.fileExtension = fileExtension;
        this.size = size;
        this.webViewLink = webViewLink;
    }

    public GoogleDriveFileVO() {
		// 생성자
	}

	public String getId() { return id; } //구글 파일 아이디
    public String getName() { return name; }
    public DateTime getCreatedTime() { return createdTime; }
    public String getFileExtension() { return fileExtension; }
    public long getSize() { return size; }
    public String getWebViewLink() { return webViewLink; }
}
