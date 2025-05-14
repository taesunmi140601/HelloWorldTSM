/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 17.     	SJH            최초 생성
 *
 * </pre>
 */
/** 
 * 회의실 상세 정보를 모달로 로딩
 */
function loadRoomDetail(roomNo) {
    fetch(`${contextPath}/${companyNo}/meetingroom/${roomNo}`)
        .then(response => {
            if (response.ok) {
                return response.text();  // 응답을 HTML로 변환
            }
            throw new Error('회의실 정보를 불러오는 데 실패했습니다.');
        })
        .then(html => {
            // 응답 HTML을 모달에 삽입
            document.getElementById('roomDetailContent').innerHTML = html;
            $('#roomDetailModal').modal('show');  // 모달 열기
        })
        .catch(error => {
            alert(error.message);
        });
}
