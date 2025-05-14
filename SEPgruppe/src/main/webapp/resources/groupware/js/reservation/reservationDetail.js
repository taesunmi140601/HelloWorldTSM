/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 8.     	SJH            최초 생성
 *
 * </pre>
 */
// 모달이 열릴 때 시간 드롭다운 생성 및 선택값 지정

$(document).on("shown.bs.modal", "#reservationDetailModal", function () {
	setTimeout(() => {
		initHourDropdown("sel_start_time");
		initHourDropdown("sel_end_time");
	}, 300);
	
    const deform = document.getElementById("reservationUpdateForm");
    if (deform) {
        deform.addEventListener("submit", function (e) {
            const destart = document.getElementsByClassName("sel_start_time").value;
            const deend = document.getElementsByClassName("sel_end_time").value;

            if (destart >= deend) {
                alert("⚠ 시작 시간은 종료 시간보다 빨라야 합니다.");
                e.preventDefault();
            }
        });
    }
	
});

/**
 * 드롭다운 시간 옵션을 생성하고 선택값 설정
 * @param {string} selectId - select 요소의 ID
 */
function initHourDropdown(selectId) {
    const deselect = document.getElementsByClassName(selectId)[0];
	
    if (!deselect) return;
	
    const deselectedValue = deselect.getAttribute("data-value");
    deselect.innerHTML = ""; // 초기화

    for (let dehour = 9; dehour <= 19; dehour++) {
        const devalue = dehour.toString().padStart(2, '0') + ":00";
        const delabel = (dehour < 12 ? "오전 " : "오후 ") + (dehour % 12 === 0 ? 12 : dehour % 12) + "시";
        const deoption = new Option(delabel, devalue);
        if (devalue === deselectedValue) deoption.selected = true;
        deselect.appendChild(deoption);
    }
}






