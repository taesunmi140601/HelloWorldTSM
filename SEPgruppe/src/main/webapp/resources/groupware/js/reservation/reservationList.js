/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 19.     	KKM            최초 생성
 *  2025. 4.  2.     	SJH           수정 중
 * </pre>
 */

// 날짜 이동 처리
function changeDate(offset) {
	const dateText = document.getElementById("currentDate")?.innerText || document.body.dataset.currentDate || "";
	if (!dateText) {
		console.error("❌ currentDate가 정의되지 않았습니다.");
		return;
	}
	const date = new Date(dateText);
	date.setDate(date.getDate() + offset);
	const formattedDate = date.toISOString().split("T")[0];

	const companyNo = window.location.pathname.split("/")[2]; // /sep/{companyNo}/reservation
	window.location.href = `/sep/${companyNo}/reservation?date=${formattedDate}`;
}

// 예약 모달 열기
function openReservationModal(roomNo, time) {
	const date = document.getElementById("currentDate")?.innerText || document.body.dataset.currentDate;
	if (!roomNo || !time || !date) {
		alert("예약 정보를 불러올 수 없습니다. 필수 값 누락.");
		return;
	}
	fillReservationTime(roomNo, date, time);
	const modal = document.getElementById("reservationModal");
	if (modal) modal.style.display = 'block';
	else $('#reservationModal').modal('show');
}

// 모달 시간 세팅
function fillReservationTime(roomNo, date, time) {
	document.getElementById("roomNo").value = roomNo;
	document.getElementById("reservationDate").value = date;
	document.getElementById("displayTime").value = `${date} ${time}`;

	const hour = parseInt(time.split(":")[0]);
	const start = `${date} ${hour.toString().padStart(2, '0')}:00:00`;
	const end = `${date} ${(hour + 1).toString().padStart(2, '0')}:00:00`;

	document.getElementById("reservationStartTime").value = start;
	document.getElementById("reservationEndTime").value = end;
}

// 모달 닫기
function closeReservationModal() {
	const modal = document.getElementById("reservationModal");
	if (modal) modal.style.display = 'none';
	else $('#reservationModal').modal('hide');
}

// 폼 액션 설정 (동적으로 companyNo 지정)
document.addEventListener("DOMContentLoaded", function() {
	const form = document.getElementById("reservationForm");
	const companyNo = window.location.pathname.split("/")[2];
	if (form && companyNo) {
		form.action = `/sep/${companyNo}/reservation/new`;
	}
});

// 상세 모달 열기 (예약 상세 보기)
$(document).on("click", ".open-detail-modal", function() {
	const reservationNo = $(this).data("reservation-no");
	const companyNo = window.location.pathname.split("/")[2];

	fetch(`/sep/${companyNo}/reservation/${reservationNo}`)
		.then(response => response.text())
		.then(html => {
			$("#reservationDetailModalContent").html(html);
			$("#reservationDetailModal").modal("show");
		})
		.catch(err => {
			console.error("상세 모달 로딩 실패", err);
			alert("❌ 예약 상세 정보를 불러오는 데 실패했습니다.");
		});
});

function openReservationDetailModal(companyNo, reservationNo) {
    fetch(`/${companyNo}/reservation/${reservationNo}`)
        .then(response => response.text())
        .then(html => {
            document.querySelector("#reservationDetailModal .modal-content").innerHTML = html;
            const modal = new bootstrap.Modal(document.getElementById("reservationDetailModal"));
            modal.show();
        })
        .catch(error => {
            console.error("❌ 예약 상세 정보 불러오기 실패:", error);
            alert("예약 정보를 불러올 수 없습니다.");
        });
}
