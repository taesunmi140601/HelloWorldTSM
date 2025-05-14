document.addEventListener('DOMContentLoaded', function() {


    let calendarEl = document.getElementById('calendar'); // 캘린더 요소

	document.body.addEventListener("click", function(event) { //삭제버튼클릭
	    let targetId = event.target.id;
		if (targetId === "schdlEdit") { //수정
		    updateSchedule();
		}
	    if (targetId === "deleteSchdl") { //삭제
	        deleteSchedule();
	    }
	});

    // FullCalendar 생성
    let calendar = new FullCalendar.Calendar(calendarEl, {
        plugins: [ FullCalendar.Interaction.default ], // 인터랙션 플러그인 추가
        initialView: 'dayGridMonth',
		headerToolbar: {
		            left: 'prev,next today',
		            center: 'title',
		            right: 'dayGridYear,dayGridMonth,dayGridDay,listWeek'
		},
        locale: 'ko',
        editable: true, // 캘린더 내에서 이벤트 이동 가능
        droppable: true, // 외부 이벤트 드롭 가능
        events: clist, // 기존 이벤트 리스트

		// 일정 클릭 시 모달 호출 (상세조회 이벤트)
        eventClick: function(info) {
            let eventUrl = info.event.url;
            info.jsEvent.preventDefault();

            if (eventUrl) {
                $.ajax({
                    url: eventUrl,
                    dataType: "html",
                    success: function(detail) {
                        $("#scheduleModal .modal-body").html(detail);
						$("#scheduleModal .modal-title").html("일정 상세");
                        $("#scheduleModal").modal("show");
                    },
                    error: function(xhr, status, error) {
                        alert("일정 상세 정보를 불러오는 데 실패했습니다.");
                    }
                });
            }
        },

		eventDidMount: function(info) {
			// `scheduleTypeNo`을 가져옴
			let typeNo = info.event.extendedProps.scheduleTypeNo;
				console.log("scheduleTypeNo", typeNo);
			// `scheduleTypeNo`를 이용해 `scheduleTypeNm` 찾기
			let typeNm = typeList.find(type => type.scheduleTypeNo == typeNo)?.scheduleTypeNm || "";
			console.log("✅ 매칭된 scheduleTypeNm:", typeNm);

			if (typeNm.includes('개인')) {
			    info.el.style.backgroundColor = 'red';
				info.el.style.color = 'white';
			} else if (typeNm.includes('부서')) {
			    info.el.style.backgroundColor = 'green';
				info.el.style.color = 'white';
			} else if (typeNm.includes('사내')) {
			    info.el.style.backgroundColor = 'lightblue';
				info.el.style.color = 'white';
			} else {
			    info.el.style.backgroundColor = 'orange';
				info.el.style.color = 'black';
			}

			const title = info.event.extendedProps.schdlNm || "";
			    const description = info.event.extendedProps.schdlCn || "";

			    info.el.innerHTML = `
			        <div style="font-weight: bold;">${title}</div>
			        <div style="font-size: 12px; white-space: normal;">${description}</div>
			    `;

			    info.el.setAttribute('title', typeNm); // 툴팁
		},


        // 일정 추가 모달 열기
        dateClick: function (info) {

            $("#scheduleModal .modal-body").empty().html("");
            $("#scheduleModal .modal-title").html("일정 등록");
            $("#scheduleModal").modal("show");
            $.ajax({
                url: `${contextPath}/${companyNo}/schedule/form`, // JSP 폼을 가져오는 URL
                dataType: "html",
                success: function (resp) {
                    $("#scheduleModal .modal-body").empty().html(resp);
                    $("#scheduleModal").modal("show");

                    // 일정 시작일 필드 자동 설정
                    $("#scheduleModal").on("shown.bs.modal", function () {
                        $("#schdlBgngYmd").val(info.dateStr); // 선택한 날짜 자동 입력
                    });

                    // 폼 제출 이벤트
                    $("#scheduleModal").on("submit", "#scheduleForm", function (event) {
                        event.preventDefault(); // 기본 폼 제출 방지

                        let startDate = new Date($("#schdlBgngYmd").val());
                        let endDate = new Date($("#schdlEndYmd").val());

                        if (endDate < startDate) {
                            Swal.fire({
                                icon: "warning",
                                title: "날짜 오류",
                                text: "종료일은 시작일보다 이후여야 합니다.",
                                confirmButtonText: "확인",
                                confirmButtonColor: "#ff9800",
                            });
                            return; // 폼 제출 중단
                        }
                        saveSchedule();
                    });
                },
                error: function (xhr, status, error) {
                    alert("일정 등록 폼을 불러오는 데 실패했습니다.");
                }
            });
        }
    });

    calendar.render();

	let filteredType = null;

	   document.querySelectorAll('.legendItem').forEach(item => {
	       item.addEventListener('click', function () {
	           const selectedType = this.dataset.type;

	           // 클릭된 유형의 active 클래스 토글
	           document.querySelectorAll('.legendItem').forEach(el => el.classList.remove('active'));
	           this.classList.add('active');

	           if (filteredType === selectedType) {
	               // 다시 클릭하면 전체 일정 보이기
	               calendar.removeAllEvents();
	               calendar.addEventSource(clist);
	               filteredType = null;
	               this.classList.remove('active');
	           } else {
	               // 해당 유형만 필터링해서 보여주기
	               const filtered = clist.filter(event => {
	                   const type = typeList.find(t => t.scheduleTypeNo == event.scheduleTypeNo);
	                   return type && type.scheduleTypeNm === selectedType;
	               });

	               calendar.removeAllEvents();
	               calendar.addEventSource(filtered);
	               filteredType = selectedType;
	           }
	       });
	   });

	// 일정 저장 함수 (POST 요청)
	function saveSchedule() {
	    let formData = $("#scheduleForm").serialize(); // 폼 데이터 직렬화

	    $.ajax({
	        url: `${contextPath}/${companyNo}/schedule/createSchedule.ajax`, // 일정 저장 API URL
	        type: "POST",
	        data: formData,
	        dataType: "json",
	        success: function(resp) {
				if (resp.cnt == 1) {
				    alert("일정이 성공적으로 저장되었습니다!");
				    $("#scheduleModal").modal("hide"); // 모달 닫기

					clist.push({
	                    start: resp.schedule.schdlBgngYmd,
	                    end: resp.schedule.schdlEndYmd,
	                    schdlNm: resp.schedule.schdlNm,
	                    schdlCn: resp.schedule.schdlCn,
	                    schdlLocation: resp.schedule.schdlLocation,
	                    createDate: resp.schedule.schdlCreateDate,
	                    schdlStatus: resp.schedule.schdlStatus,
	                    scheduleTypeNo: resp.schedule.scheduleTypeNo,
	                    url: `${contextPath}/${companyNo}/schedule/scheduleDetail.do?what=${resp.schedule.schdlNo}`,
	                    groupId: clist.length // 새로운 groupId 생성
	                });

				    location.reload(); // 캘린더 새로고침
				} else {
				    alert("일정 저장에 실패했습니다. 다시 시도해주세요.");
				}
	        },
	        error: function(xhr, status, error) {
	            console.error("AJAX 오류:", status, error);
	            alert("일정 저장 중 오류가 발생했습니다.");
	        }
	    });
	}

	let selectedSchdlNo = null; // 전역 변수

	// 수정버튼
	function editSchedule() {


		selectedSchdlNo = selectedRadio.value; // 전역 변수에 저장

	    // 기존 일정 데이터 불러오기
	    $.ajax({
	        url: `${contextPath}/${companyNo}/schedule/scheduleEdit/${selectedSchdlNo}`,
	        dataType: "json",
	        success: function(data) {

	            if (data) {
					$("#scheduleModal .modal-body").empty().html;
					$("#scheduleModal .modal-title").html("일정 수정");
	                $("#schdlNo").val(data.schdlNo);
	                $("#empId").val(data.empId);
	                $("#schdlNm").val(data.schdlNm);
	                $("#schdlCn").val(data.schdlCn);
	                $("#schdlBgngYmd").val(data.schdlBgngYmd);
	                $("#schdlEndYmd").val(data.schdlEndYmd);
	                $("#schdlLocation").val(data.schdlLocation);
	                $("#schdlCreateDate").val(data.schdlCreateDate);
	                $("#schdlStatus").val(data.schdlStatus);
	                $("#schdlUpdateDate").val(data.schdlUpdateDate);
	                $("#scheduleModal").modal("show");
	            }
	        },
	        error: function(xhr, status, error) {
	            Swal.fire("오류 발생!", "일정 정보를 불러오는 데 실패했습니다.", "error");
	        }
	    });
	}



	// 수정할 일정 선택
	$(document).on("click", "#editSchdl", function() {
		let selectedRadio = document.querySelector('input[name="schdlEdit"]:checked');

		if (!selectedRadio) {
		    Swal.fire({
		        icon: "warning",
		        title: "체크 필요!",
		        text: "수정할 일정을 선택하세요!",
		        confirmButtonText: "확인",
		        confirmButtonColor: "#ff9800",
		    });
		    return;
		}
		var schdlNo = $('input[name="schdlEdit"]:checked').val();

		$.ajax({
		    url: `${contextPath}/${companyNo}/schedule/formEdit?schdlNo=${schdlNo}`,  // JSP 폼을 가져오는 URL
		    dataType: "html",
		    success: function(resp) {

		        $("#scheduleModal .modal-body").empty().html(resp);
				$("#scheduleModal .modal-title").html("일정 수정");
		        $("#scheduleModal").modal("show");
				console.log("안녕?");

		    },
		    error: function(xhr, status, error) {
		        console.error("AJAX 오류:", status, error);
		        alert("일정 수정 폼을 불러오는 데 실패했습니다.");
		    }
		});
	});

	// 일정 업데이트 함수
	function updateSchedule() {
		var schdlNo = $('input[name="schdlNo"]').val();
	    let formData = {
	        schdlNo: schdlNo,
	        schdlNm: $("#schdlNm").val(),
	        schdlCn: $("#schdlCn").val(),
			scheduleTypeNo: $("#scheduleTypeNo").val(),
	        schdlBgngYmd: $("#schdlBgngYmd").val(),
	        schdlEndYmd: $("#schdlEndYmd").val(),
	        schdlLocation: $("#schdlLocation").val(),
	        schdlStatus: $("#schdlStatus").val(),
	        empId: $("#empId").val()
	    };

	    $.ajax({
	        url: `${contextPath}/${companyNo}/schedule/updateSchedule.ajax`,
	        type: "PUT",
	        contentType: "application/json",
	        data: JSON.stringify(formData),
	        dataType: "json",
	        success: function(resp) {
	            if (resp.cnt === 1) {
	                Swal.fire("수정 완료!", "일정이 성공적으로 수정되었습니다!", "success")
	                    .then(() => {
							// 수정일시를 현재 날짜로 업데이트
	                       $("#schdlUpdateDate").text(resp.schdlUpdateDate); // 수정일시 업데이트

	                        $("#scheduleModal").modal("hide");
	                        location.reload();

	                    });
	            } else {
	                Swal.fire("수정 실패!", "다시 시도해 주세요.", "error");
	            }
	        },
	        error: function(xhr, status, error) {
	            console.error("AJAX 오류:", status, error);
	            Swal.fire("오류 발생!", "일정 수정 중 문제가 발생했습니다.", "error");
	        }
	    });
	}

	//삭제
	function deleteSchedule() {

	    let selectedRadio = document.querySelector('input[name="schdlEdit"]:checked');

	    if (!selectedRadio) {
	        Swal.fire({
	            icon: "warning",
	            title: "체크 필요!",
	            text: "삭제할 일정을 선택하세요!",
	            confirmButtonText: "확인",
	            confirmButtonColor: "#ff9800",
	        });
	        return;
	    }

	    let schdlNo = selectedRadio.value; // 선택된 일정 번호

	    Swal.fire({
	        title: "삭제 확인",
	        text: "정말로 삭제하시겠습니까?",
	        icon: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#d33",
	        cancelButtonColor: "#3085d6",
	        confirmButtonText: "삭제",
	        cancelButtonText: "취소"
	    }).then((result) => {
	        if (result.isConfirmed) {
	            $.ajax({
	                url: `${contextPath}/${companyNo}/schedule/deleteSchedule.ajax/${schdlNo}`,
	                type: "DELETE",
	                success: function(resp) {
	                    if (resp.cnt == 1) {
	                        Swal.fire("삭제 완료!", "일정이 삭제되었습니다.", "success").then(() => {
	                            location.reload(); // 페이지 새로고침
	                        });
	                    } else {
	                        Swal.fire("삭제 실패!", "다시 시도해 주세요.", "error");
	                    }
	                },
	                error: function(xhr, status, error) {
	                    Swal.fire("오류 발생!", "일정 삭제 중 문제가 발생했습니다.", "error");
	                }
	            });
	        }
	    });
	}

});