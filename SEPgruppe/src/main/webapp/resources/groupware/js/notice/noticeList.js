/**
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 14.     	KMJ            최초 생성
 *
 * </pre>
 */

/* 공지사항 분류 선택 */
function fn_change(obj) {
    var category = document.querySelector('input[name="category"]:checked').value;
    location.href = `/sep/${companyNo}/notice?category=` + category;
}


document.addEventListener('DOMContentLoaded', function(){
	const deleteBtn = document.querySelector('#deleteButton'); // List Delete 버튼
	const confirmDelete = document.querySelector('#confirmDelete'); // Modal 삭제 버튼
	const noticeInput = document.querySelector('#noticeNoInput'); // Modal 글번호 삽입되는 Input 태그
	const deleteNoticeModal = document.querySelector('#deleteNoticeModal'); // 모달 선택
	let noticeNo = [];
	const selectAll = document.querySelector('#select-all'); // 전체 체크박스
	const noticCheck = document.querySelectorAll('.noticCheck'); // 모든 체크박스

	// Delete버튼 이벤트
	deleteBtn.addEventListener('click', function(){

		noticeNo = []; // 글번호 초기화

		const selectNotice = document.querySelectorAll("input[name='noticeSelect']:checked"); // 체크된 공지사항 ID 가져오기
		console.log("공지사항 글번호 있는가", selectNotice);

		/* 선택한 공지사항이 없을 때 */
		if(selectNotice.length === 0){
			Swal.fire({
			  title: "삭제 실패",
			  text: "선택된 공지사항이 없습니다.",
			  icon: "question"
			});
			return;
		}

		// 선택된 공지사항번호를 배열에 담기
		selectNotice.forEach(function(checkbox){
			let noticeId = checkbox.value; // 체크한 글번호 10진수로 변환
			noticeNo.push(noticeId);
		});
		console.log(noticeNo); // 콘솔에 선택한 공지사항 번호 정상 출력됨

		// 배열 내 각 요소의 타입 출력 (로깅용)
		noticeNo.forEach((item, index) => {
		    console.log(`noticeNo[${index}] 타입: ${typeof item}`);  // string타입으로 확인
		});

		// 글번호 value값에 글번호 삽입
		noticeInput.value = noticeNo.join(',');
		// document.querySelector('#noticeNoInput').value = noticeNo.join(',');
		console.log("입력된 데이터 타입: ", typeof noticeInput.value); // string 타입

		$('#deleteNoticeModal').modal('show'); // 모달 열기

    });

	// Modal 닫힐 때 글번호 Input 태그 초기화
	deleteNoticeModal.addEventListener('hidden.bs.modal', function(){
		noticeNo = [];
		noticeInput.value = '';
	})


	/* 게시글 삭제 시 로그인한 ID와 작성자의 ID 일치하는지 검증 */
	confirmDelete.addEventListener('click', async function(event){
		event.preventDefault(); // 폼 전송 중지

		const loginEmpId = document.querySelector('input[name="empId"]').value; // 로그인한 사용자 ID
		console.log(loginEmpId); // 정상 출력됨

		for(const notice of noticeNo){
			try{
				const response = await fetch(`/sep/${companyNo}/notice/${noticeNo}/select`); // 서버에서 공지사항 정보 가져오기

				const noticeVo = await response.json(); // 서버에서 가져온 공지사항을 json형식으로 저장

				if(noticeVo.empId !== loginEmpId){
					Swal.fire({
					  icon: "error",
					  title: "삭제 실패",
					  text: "글 작성자만 삭제 가능합니다.",
					});
					return;
				}
			}catch(error){
				const deleteForm = document.forms['deleteForm']; // 폼 선택
				deleteForm.submit(); // 폼 제출

			}

		}
		// 모든 검증이 끝나면, 스윙을 닫고 폼 제출
		Swal.close(); // 로딩을 종료
		Swal.fire({
			title: "삭제 성공!",
			icon: "success",
			text: "선택한 공지사항이 삭제되었습니다.",
		}).then(() => {
			// 폼 제출
			const deleteForm = document.forms['deleteForm']; // 폼 선택
			deleteForm.submit(); // 폼 제출
		});
	});


	/* 전체 체크박스 클릭 시 해당 페이지 전체 체크되는 기능 */
	selectAll.addEventListener('click', function(){
		noticCheck.forEach(function(checkbox){
			checkbox.checked = selectAll.checked; // #select-all의 체크 상태로 모두 설정

		});
	});
});
