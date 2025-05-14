package kr.or.ddit.works.address.service;

import java.util.List;

import kr.or.ddit.works.address.vo.AddressbookVO;

/**
 * 주소록 서비스
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
public interface AddressbookService {

    /**
     * 주소록 전체 목록 조회
     * @return List<AddressbookVO>
     */
    public List<AddressbookVO> selectAllListAddress();

    /**
     * 주소록 상세 조회
     * @param adbkNo 주소록 번호
     * @return AddressbookVO
     */
    public AddressbookVO selectAddressDetail(int adbkNo);

    /**
     * 주소록 추가
     * @param addressbookVo 주소록 객체
     * @return 성공 시 1, 실패 시 0
     */
    public boolean insertAddress(AddressbookVO addressbookVo);

    /**
     * 주소록 수정
     * @param addressbookVo 주소록 객체
     * @return 성공 시 1, 실패 시 0
     */
    public boolean updateAddress(AddressbookVO addressbookVo);

    /**
     * 주소록 삭제
     * @param addressbookVo 주소록 객체
     * @return 성공 시 1, 실패 시 0
     */
    public boolean deleteAddress(AddressbookVO addressbookVo);
}