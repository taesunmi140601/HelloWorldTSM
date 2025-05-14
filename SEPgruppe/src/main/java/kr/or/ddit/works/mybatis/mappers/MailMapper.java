package kr.or.ddit.works.mybatis.mappers;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.mail.vo.MailReceptionVO;
import kr.or.ddit.works.mail.vo.MailSentLogVO;
import kr.or.ddit.works.mail.vo.MailUserAuthVO;

/**
 * 메일 매퍼
 * @author JYS
 * @since 2025. 3. 16.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 16.     	JYS	          최초 생성
 *
 * </pre>
 */
@Mapper
public interface MailMapper {
	
	List<String> selectFavoriteMailIds(String empId);

	void insertFavorite(Map<String, Object> map);
	
    void deleteFavorite(Map<String, Object> map);
	
    List<MailReceptionVO> selectFavoriteMails(String userId);
    
    List<MailReceptionVO> selectFavoriteMailList(String empId);
    
    public List<MailReceptionVO> selectMailList(@Param("empId") String empId);
    
    public List<MailSentLogVO> selectSendMailList(@Param("empId") String empId);
    
    public MailUserAuthVO selectTokenByUserId(@Param("empId") String empId);
    
    public int insertToken(MailUserAuthVO token);
    
    public int updateToken(MailUserAuthVO token);
    
    public int insertMailList(MailReceptionVO mailList);
    
    public int insertMailLog(MailSentLogVO mail);
    
    


}
