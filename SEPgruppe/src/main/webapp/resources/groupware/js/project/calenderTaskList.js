/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 26.     	KKM            최초 생성
 *
 * </pre>
 */
document.addEventListener('DOMContentLoaded', function() {
    let calendarEl = document.getElementById('calendar'); // 캘린더 요소
	
    // FullCalendar 생성
    let calendar = new FullCalendar.Calendar(calendarEl, {
        plugins: [ FullCalendar.Interaction.default ], // 인터랙션 플러그인 추가
        initialView: 'dayGridMonth',
        locale: 'ko',
        editable: false, // 캘린더 내에서 이벤트 이동 비활성화
        droppable: false, // 외부 이벤트 드롭 비활성화

        // clist를 사용하여 이벤트 추가
        events: clist,

        // 일정 클릭 시 상세보기 모달 호출
        eventClick: function(info) {
            let eventUrl = info.event.url;
            info.jsEvent.preventDefault(); // 기본 클릭 동작 방지

            if (eventUrl) {
                $.ajax({
                    url: eventUrl,
                    type: "GET",
                    dataType: "html",
                    success: function(detail) {
                        $("#scheduleModal .modal-body").html(detail);
                        $("#scheduleModal").modal("show");
                    },
                    error: function(xhr, status, error) {
                        console.error("AJAX 오류:", status, error);
                        alert("일정 상세 정보를 불러오는 데 실패했습니다.");
                    }
                });
            }
        }
    });

    calendar.render(); // 캘린더 렌더링
});




