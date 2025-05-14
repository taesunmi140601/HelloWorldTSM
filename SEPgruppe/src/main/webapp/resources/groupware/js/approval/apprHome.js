/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 20.     	JYS            최초 생성
 *
 * </pre>
 */
// DOMContentLoaded 이벤트가 발생하면 실행
let path = document.body.getAttribute('data-context-path');

document.addEventListener("DOMContentLoaded", () => {
    // 모든 문서에 대해 상세보기 함수 실행
    allDocDetail();
});



// 모든 문서 클릭 시 상세보기로 이동
function allDocDetail() {
	document.addEventListener("click", function(event) {
	        // <tr data-doc-no> 요소 처리
	        const tr = event.target.closest("tr[data-doc-no]");
	        if (tr) {
	            const aprvlDocNo = tr.dataset.docNo;
	            location.href = `${path}/apprHome/aprvlDocDetail/${aprvlDocNo}`;
	            return;
	        }

	        // .card[data-doc-no] 요소 처리
	        const card = event.target.closest(".card[data-doc-no]");
	        if (card) {
	            const aprvlDocNo = card.dataset.docNo;
	            location.href = `${path}/apprHome/aprvlDocDetail/${aprvlDocNo}`;
	        }
	    });
}

// 성공 여부 모달 
function showToast(content){
	const apprResToast = document.querySelector("#apprResToast");
	const toastBody = apprResToast.querySelector(".toast-body");
	toastBody.innerText = content;
	
	const bsToast = new bootstrap.Toast(apprResToast); // 부트스트랩 Toast 객체 생성
	bsToast.show();
}