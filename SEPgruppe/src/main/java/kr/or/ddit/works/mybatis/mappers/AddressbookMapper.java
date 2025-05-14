package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.address.vo.AddressbookVO;

/**
 * 주소록 매퍼
 * 
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
public interface AddressbookMapper {
	
	/**
	 * 주소록 전체 목록 조회
	 * @return
	 */
	public List<AddressbookVO> selectAllListAddress();
	
	/**
	 * 주소록 상세 조회
	 * @param adbkNo 주소록 번호
	 * @return
	 */
	public AddressbookVO selectAddressDetail(@Param("adbkNo") int adbkNo);
	
	
	/**
	 * 주소록 추가
	 * @param AddressbookVo
	 * @return
	 */
	public int insertAddress(AddressbookVO AddressbookVo);
	
	/**
	 * 주소록 수정
	 * @param AddressbookVo
	 * @return
	 */
	public int updateAddress(AddressbookVO AddressbookVo);
	
	/**
	 *  주소록 삭제
	 * @param AddressbookVo
	 * @return
	 */
	public int deleteAddress(AddressbookVO AddressbookVo);

}
