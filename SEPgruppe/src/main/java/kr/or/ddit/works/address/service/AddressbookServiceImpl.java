package kr.or.ddit.works.address.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.address.vo.AddressbookVO;
import kr.or.ddit.works.mybatis.mappers.AddressbookMapper;

@Service
public class AddressbookServiceImpl implements AddressbookService {

    @Autowired
    private AddressbookMapper addressbookMapper;

    /**
     * 주소록 전체 목록 조회
     * @return 주소록 목록(List<AddressbookVO>)
     */
    @Override
    public List<AddressbookVO> selectAllListAddress() {
        return addressbookMapper.selectAllListAddress();
    }

    /**
     * 주소록 상세 조회
     * @param adbkNo 주소록 번호
     * @return 조회된 주소록 정보(AddressbookVO)
     */
    @Override
    public AddressbookVO selectAddressDetail(int adbkNo) {
        return addressbookMapper.selectAddressDetail(adbkNo);
    }

    /**
     * 주소록 추가
     * @param addressbookVo 추가할 주소록 객체
     * @return 성공 시 true, 실패 시 false
     */
    @Override
    public boolean insertAddress(AddressbookVO addressbookVo) {
        return addressbookMapper.insertAddress(addressbookVo) > 0;
    }

    /**
     * 주소록 수정
     * @param addressbookVo 수정할 주소록 객체
     * @return 성공 시 true, 실패 시 false
     */
    @Override
    public boolean updateAddress(AddressbookVO addressbookVo) {
        return addressbookMapper.updateAddress(addressbookVo) > 0;
    }

    /**
     * 주소록 삭제
     * @param addressbookVo 삭제할 주소록 객체
     * @return 성공 시 true, 실패 시 false
     */
    @Override
    public boolean deleteAddress(AddressbookVO addressbookVo) {
        return addressbookMapper.deleteAddress(addressbookVo) > 0;
    }
}
