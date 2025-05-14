/**
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 27.     	KMJ            최초 생성
 *
 * </pre>
 */

document.addEventListener("DOMContentLoaded", function () {
    const menuContainer = document.getElementById("myNavbar"); // 컨텍스트 메뉴
    let activeTr = null; // 현재 열린 tr 저장 변수


	    // 파일 업로드 버튼 이벤트 바인딩 (기존 코드 유지)
	    let uploadBtn = document.querySelector("#pills-home-tab-icon");
	    let fileInput = document.querySelector("#fileInput");
	    if (uploadBtn) {
	        uploadBtn.addEventListener("click", function () {
	            if (fileInput) {
	                fileInput.click();
	            } else {
	                console.error("파일 입력 요소(#fileInput)를 찾을 수 없습니다.");
	            }
	        });
	    } else {
	        console.error("파일 업로드 버튼(#pills-home-tab-icon)을 찾을 수 없습니다.");
	    }

	    // 파일 선택 후 업로드 실행 (기존 코드 유지)
	    if (fileInput) {
	        fileInput.addEventListener("change", function () {
	            uploadFile();
	        });
	    }

    // 파일 업로드 함수 (기존 코드 유지)
    function uploadFile() {
        let fileInput = document.querySelector("#fileInput");
        if (!fileInput || fileInput.files.length === 0) {
            alert("파일을 선택해주세요.");
            return;
        }

        let file = fileInput.files[0];
        let formData = new FormData();
        formData.append("file", file);
        formData.append("empId", empId); //로그인된 empId를 동적으로 설정해야 함

        fetch(contextPath + "/" + companyNo + "/webhard/new", {
            method: "POST",
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            Swal.fire("업로드 완료!", "파일이 성공적으로 업로드되었습니다!", "success")
            location.reload(); // 업로드 후 파일 목록 갱신
        })
        .catch(error => {
            console.error("업로드 실패:", error);
            alert("파일 업로드에 실패했습니다.");
        });
    }

    // 파일 다운로드 함수 (기존 코드 유지)
    function downloadFile(fileId) {
        const downloadUrl = contextPath + "/" + companyNo + "/webhard/download/" + fileId;

        fetch(downloadUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error("파일 다운로드 실패");
                }

                //Content-Disposition 헤더에서 파일 이름 추출
                const contentDisposition = response.headers.get("Content-Disposition");
                let fileName = "file"; //기본 파일 이름
                if (contentDisposition && contentDisposition.indexOf("filename*") > -1) {
                    const fileNameMatch = /filename\*=UTF-8''(.*)/.exec(contentDisposition);
                    if (fileNameMatch && fileNameMatch.length > 1) {
                        fileName = decodeURIComponent(fileNameMatch[1]);
                    }
                }

                return response.blob().then(blob => ({ blob, fileName }));
            })
            .then(({ blob, fileName }) => {
                //Blob 데이터를 사용하여 파일 다운로드
                const url = window.URL.createObjectURL(blob);
                const link = document.createElement("a");
                link.href = url;
                link.setAttribute("download", fileName);
                console.log("다운로드 파일 이름:", fileName);
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            })
            .catch(error => {
                console.error("파일 다운로드 중 오류 발생:", error);
                alert("파일 다운로드에 실패했습니다.");
            });
    }

    // 다운로드 버튼 클릭 이벤트 (선택된 파일 다운로드)
    const downloadSelectedBtn = document.getElementById("downloadSelectedFiles");
    if (downloadSelectedBtn) {
        downloadSelectedBtn.addEventListener("click", function () {
            const checkedFiles = document.querySelectorAll('input[name="deleteFile"]:checked');
            if (checkedFiles.length === 0) {
                Swal.fire({
                    icon: "warning",
                    title: "선택 필요!",
                    text: "다운로드할 파일을 선택하세요!",
                    confirmButtonText: "확인",
                    confirmButtonColor: "#ff9800",
                });
                return;
            }

            checkedFiles.forEach(checkbox => {
                const fileId = checkbox.value;
                console.log("다운로드 요청 - fileId:", fileId);
                downloadFile(fileId); // 각 선택된 파일에 대해 다운로드 함수 호출
            });
        });
    } else {
        console.error("다운로드 버튼(#downloadSelectedFiles)을 찾을 수 없습니다.");
    }

    // 파일 삭제 버튼 클릭 이벤트 (기존 코드 유지)
    let deleteBtn = document.querySelector("#pills-profile-tab-icon");
    if (deleteBtn) {
        deleteBtn.addEventListener("click", function () {
            let selectedFile = document.querySelector('input[name="deleteFile"]:checked');

            if (!selectedFile) {
                Swal.fire({
                    icon: "warning",
                    title: "체크 필요!",
                    text: "삭제할 파일을 선택하세요!",
                    confirmButtonText: "확인",
                    confirmButtonColor: "#ff9800",
                });
                return;
            }
            let selectedFileId = selectedFile.value; //선택된 파일 ID 가져오기

            Swal.fire({
                title: "삭제 확인",
                text: "정말로 삭제하시겠습니까?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#d33",
                cancelButtonColor: "#3085d6",
                confirmButtonText: "삭제",
                cancelButtonText: "취소",
            }).then((result) => {
                if (result.isConfirmed) {
                    deleteFile(selectedFileId); //삭제 함수 호출
                }
            });
        });
    } else {
        console.error("파일 삭제 버튼(#pills-profile-tab-icon)을 찾을 수 없습니다.");
    }

    // 파일 삭제 함수 (기존 코드 유지)
    function deleteFile(fileId) {
        $.ajax({
            url: contextPath + "/" + companyNo + "/webhard/delete/" + fileId,
            type: "DELETE",
            dataType: "JSON",
            success: function (resp) {
                if (resp) {
                    Swal.fire("삭제 완료!", "파일이 삭제되었습니다.", "success").then(() => {
                        location.reload(); //페이지 새로고침
                    });
                } else {
                    Swal.fire("삭제 실패!", "다시 시도해 주세요.", "error");
                }
            },
            error: function (xhr, status, error) {
                console.error("AJAX 오류:", status, error);
                Swal.fire("오류 발생!", "파일 삭제 중 문제가 발생했습니다.", "error");
            },
        });
    }
});