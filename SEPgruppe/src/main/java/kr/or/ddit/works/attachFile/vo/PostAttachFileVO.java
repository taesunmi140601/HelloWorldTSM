package kr.or.ddit.works.attachFile.vo;

import java.util.List;

import javax.validation.constraints.NotBlank;

import kr.or.ddit.works.notice.vo.NoticeVO;
import lombok.Data;

@Data
public class PostAttachFileVO {
	private String no; 			// 기본키
	private Long postNo; 		// 게시판 글번호
	private Long noticeNo; 		// 공지사항 글번호
	private String attachFileNo; // 첨부파일 번호

	private List<NoticeVO> notice;
	private AttachFileVO file;
}
