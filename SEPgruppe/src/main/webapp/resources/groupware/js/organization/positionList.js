/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 8.     	JSW            최초 생성
 *
 * </pre>
 */
// 전체 체크박스
$("#checkAll").on("change", function () {
    $(".checkOne").prop("checked", this.checked);
});

// 개별 체크 → 전체 체크 상태 업데이트
$(".checkOne").on("change", function () {
    const allChecked = $(".checkOne").length === $(".checkOne:checked").length;
    $("#checkAll").prop("checked", allChecked);
});

// 삭제
$("#deleteBtn").on("click", function () {
    const selected = $(".checkOne:checked").map(function () {
        return $(this).val();
    }).get();

    if (selected.length === 0) {
        Swal.fire({
            toast: true,
            icon: "warning",
            title: "삭제할 항목을 선택해주세요",
            position: "top",
            timer: 2000,
            showConfirmButton: false
        });
        return;
    }

    Swal.fire({
        title: "선택한 직위를 삭제할까요?",
        text: `${selected.length}개 항목이 삭제됩니다.`,
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "삭제",
        cancelButtonText: "취소"
    }).then(result => {
        if (result.isConfirmed) {
            $.ajax({
                url: `${contextPath}/${companyNo}/position/admin/delete`,
                type: "DELETE",
                contentType: "application/json",
                data: JSON.stringify(selected),
                success: function () {
                    Swal.fire({
                        icon: "success",
                        title: "삭제 완료",
                        timer: 1500,
                        showConfirmButton: false
                    }).then(() => location.reload());
                }
            });
        }
    });
});

// 모달 열기
$("#addBtn").on("click", () => {
    $("#positionAddModal").fadeIn();
});

// 모달 닫기
$("#cancelAdd, .close-modal").on("click", () => {
    $("#positionAddModal").fadeOut();
});

// 모달 바깥쪽 클릭 시 닫기
$(window).on("click", function (e) {
    if ($(e.target).is("#positionAddModal")) {
        $("#positionAddModal").fadeOut();
    }
});

// 등록 처리
$("#positionAddForm").on("submit", function (e) {
    e.preventDefault();

    const newPos = {
        positionCd: $("#positionCd").val().trim(),
        positionName: $("#positionName").val().trim()
    };

    $.ajax({
        url: `${contextPath}/${companyNo}/position/admin/insert`,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(newPos),
        success: function () {
            Swal.fire({
                icon: "success",
                toast: true,
                position: "top",
                title: "직위가 등록되었습니다!",
                showConfirmButton: false,
                timer: 2000
            }).then(() => location.reload());
        },
        error: function () {
            Swal.fire("오류", "등록 중 문제가 발생했습니다", "error");
        }
    });
});

let sortActive = false; // 현재 순서 변경 모드 여부
let sortableInstance = null;

// 드래그앤드롭 + 순서 변경
$("#sortBtn").on("click", function () {
	sortActive = !sortActive;
	
	if (sortActive) {
		$("#sortablePositionTable").addClass("drag-mode");
		$("#sortablePositionTable tr").addClass("drag-mode");
        $("#sortBtn").text("✔ 완료"); 
						
        Swal.fire({
            title: "순서 변경 모드 ON",
            text: "드래그로 순서를 조정하세요. 완료 시 자동 저장됩니다.",
            icon: "info",
            toast: true,
            position: "top",
            timer: 2500,
            showConfirmButton: false
        });

		if (!sortableInstance) {
            sortableInstance = Sortable.create(document.getElementById("sortablePositionTable"), {
                animation: 150,
                handle: ".drag-handle",
                onEnd: function () {
                    const sorted = [];
                    $("#sortablePositionTable tr").each(function (i) {
                        const positionCd = $(this).data("positioncd");
                        sorted.push({
                            positionCd: positionCd,
                            sortOrder: i + 1
                        });
                    });

                $.ajax({
                    url: `${contextPath}/${companyNo}/position/admin/updateSort`,
                    type: "PUT",
                    contentType: "application/json",
                    data: JSON.stringify(sorted),
                    success: function () {
                        Swal.fire({
                            toast: true,
                            icon: "success",
                            title: "순서 저장 완료",
                            position: "top",
                            showConfirmButton: false,
                            timer: 2000
                        });
                    },
					error: function () {
                            Swal.fire("에러", "순서 저장 실패", "error");
                        }
                    });
                }
            });
        }

    } else {
        // ✅ 순서 변경 완료 → 다시 기본 화면
        $("#sortablePositionTable").removeClass("drag-mode");
		$("#sortablePositionTable tr").removeClass("drag-mode");
        $("#sortBtn").text("↕ 순서 바꾸기");
        location.reload();
    }
});

document.addEventListener("DOMContentLoaded", function () {
  document.getElementById("mecro").addEventListener("click", function () {
    // 자동완성 값 설정
    const autoFillData = {
      positionCd: "P010120",
      positionName: "기술고문",
    };

    document.getElementById("positionCd").value = autoFillData.positionCd;
    document.getElementById("positionName").value = autoFillData.positionName;
  });
});