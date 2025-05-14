package kr.or.ddit.works.community.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 게시판 BOARD VO
 * @author JYS
 * @since 2025. 3. 15.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 15.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
public class BoardVO implements Serializable{

	private Long boardNo;     		 //게시판 번호
	private Long communityNo;      	 //커뮤니티 방 번호
	private String boardNm;      	 //게시판 이름
	private String boardContent;     //게시판 설명

	private List<PostVO> post;
	private CommunityVO community;
}
