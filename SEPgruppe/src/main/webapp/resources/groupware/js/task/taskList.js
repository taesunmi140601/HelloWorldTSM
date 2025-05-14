/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 18.     	KMJ            최초 생성
 *
 * </pre>
 */

document.addEventListener("DOMContentLoaded", function () {
    const checkAll = document.getElementById("checkAll");
    const checkboxes = document.querySelectorAll(".taskChk");
    const deleteBtn = document.getElementById("deleteTaskBtn");

	const currentUserId = document.body.dataset.empId;
	
	if (checkAll) {
        const isProjectOwner = checkAll.dataset.projectOwner === currentUserId;

        if (isProjectOwner) {
            checkAll.addEventListener("change", function () {
                checkboxes.forEach(chk => chk.checked = this.checked);
            });
        } else {
            checkAll.disabled = true;
        }
    }
	
    // 개별 체크 시 전체선택 해제 처리
    checkboxes.forEach(chk => {
        chk.addEventListener("change", function () {
            if (!this.checked) {
                checkAll.checked = false;
            } else if (document.querySelectorAll(".taskChk:checked").length === checkboxes.length) {
                checkAll.checked = true;
            }
        });
    });

    deleteBtn.addEventListener("click", function () {
        const selected = document.querySelectorAll(".taskChk:checked");

        if (selected.length === 0) {
            Swal.fire({
                toast: true,
                position: 'top',
                icon: "warning",
                title: "삭제할 일감을 선택하세요!",
                showConfirmButton: false,
                timer: 2000
            });
            return;
        }

        Swal.fire({
            title: "정말 삭제하시겠습니까?",
            text: "삭제하면 되돌릴 수 없습니다.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#aaa",
            confirmButtonText: "삭제",
            cancelButtonText: "취소"
        }).then(result => {
            if (result.isConfirmed) {
                const companyNo = getPathSegment(1); // ex: /sep/...
                const projectNo = new URLSearchParams(window.location.search).get("projectNo");

                const taskNos = Array.from(selected).map(chk => chk.value);

                fetch(contextPath + `/${companyNo}/task/ajax/delete`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        [csrfHeaderName]: csrfToken
                    },
                    body: JSON.stringify({ taskNos, projectNo })
                })
                .then(res => {
                    if (!res.ok) throw new Error("삭제 실패");
                    return res.text();
                })
                .then(() => {
                    taskNos.forEach(taskNo => {
                        const row = document.querySelector(`tr[data-task-no="${taskNo}"]`);
                        if (row) row.remove();
                    });

                    Swal.fire({
                        toast: true,
                        position: 'top',
                        icon: "success",
                        title: "삭제가 완료되었습니다.",
                        showConfirmButton: false,
                        timer: 2000
                    });
                })
                .catch(() => {
                    Swal.fire("오류", "일감 삭제 중 오류가 발생했습니다.", "error");
                });
            }
        });
    });

    function getPathSegment(index) {
        return window.location.pathname.split("/")[index];
    }
});