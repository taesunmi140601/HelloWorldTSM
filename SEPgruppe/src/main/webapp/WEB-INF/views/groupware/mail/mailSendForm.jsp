<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 4. 11.     	손현진            최초 생성
 *
-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="content-area">
  <div class="compose-mail-wrapper">
    <h3><i class="fa-solid fa-envelope"></i> 메일 작성</h3>
    <!-- enctype="multipart/form-data" 제거 (직접 FormData 생성할 것이므로) -->
    <form id="sendMailForm" class="compose-mail-form" method="post">
        <input type="hidden" name="empId" value="${realUser.empId}">

        <label for="toEmail">받는 사람</label>
        <input type="email" name="toEmail" id="toEmail" required>

        <label for="mailSubject">제목</label>
        <input type="text" name="mailSubject" id="mailSubject" required>

        <label for="sentCotentLog">내용</label>
        <textarea name="sentCotentLog" id="sentCotentLog" rows="10" required></textarea>

        <!-- 파일 선택 버튼 & 숨겨진 파일 input -->
        <label>첨부파일 추가</label>
        <button type="button" id="btnAddFile">파일 선택</button>
        <!-- 숨겨진 multiple file input -->
        <input type="file" id="hiddenFileInput" multiple style="display: none;" />

        <!-- 선택된 파일 목록을 보여줄 영역 -->
        <div id="attachmentList" style="margin-top: 10px;"></div>

        <!-- 최종 전송 버튼 -->
        <button type="submit">보내기</button>
    </form>
  </div>
</div>

<script>
$(document).ready(function() {
    const btnAddFile = document.getElementById('btnAddFile');
    const hiddenFileInput = document.getElementById('hiddenFileInput');
    const attachmentList = document.getElementById('attachmentList');
    const sendMailForm = document.getElementById('sendMailForm');

    // 사용자가 선택한 파일들을 관리할 배열
    let selectedFiles = [];

    // "파일 선택" 버튼 클릭 -> 숨겨진 input 클릭 트리거
    btnAddFile.addEventListener('click', () => {
        hiddenFileInput.click();
    });

    // 파일 input에서 파일을 선택했을 때
    hiddenFileInput.addEventListener('change', (e) => {
        // 새로 선택된 파일들(복수 가능)을 selectedFiles에 추가
        for (let file of e.target.files) {
            selectedFiles.push(file);
        }
        // UI 업데이트
        updateFileList();
        // 선택 끝낸 뒤, input.value 초기화(같은 파일 다시 선택 허용 위해)
        hiddenFileInput.value = '';
    });

    // 파일 목록(UI) 갱신 함수
    function updateFileList() {
        attachmentList.innerHTML = ''; // 초기화

        selectedFiles.forEach((file, index) => {
            const fileWrapper = document.createElement('div');
            fileWrapper.style.marginBottom = '5px';

            const fileNameSpan = document.createElement('span');
            fileNameSpan.textContent = file.name + ' ';

            // 삭제(X) 버튼
            const removeBtn = document.createElement('button');
            removeBtn.type = 'button';
            removeBtn.textContent = 'X';
            removeBtn.style.marginLeft = '8px';
            removeBtn.addEventListener('click', () => {
                // 배열에서 index번째 파일 제거
                selectedFiles.splice(index, 1);
                updateFileList();
            });

            fileWrapper.appendChild(fileNameSpan);
            fileWrapper.appendChild(removeBtn);
            attachmentList.appendChild(fileWrapper);
        });
    }

    // 폼 전송 로직 (Ajax)
    sendMailForm.addEventListener('submit', (e) => {
        e.preventDefault();

        const formData = new FormData(sendMailForm);

        // selectedFiles에 담긴 파일들을 formData에 추가
        selectedFiles.forEach(file => {
            formData.append('attachments', file);
        });

        // Ajax로 전송
        fetch(contextPath + "/" + companyNo + "/mail/send", {
            method: 'POST',
            body: formData
        })
        .then(res => res.text())
        .then(result => {
            if(result === 'success') {
            	Swal.fire({
    				toast: true,
    				position: 'top',
    				title: "메일이 성공적으로 전송되었습니다.",
    				icon: "success",
    				draggable: true,
    				showConfirmButton: false,
    				timer: 3000
    			});
                location.href = contextPath + "/" + companyNo + "/mail";
            } else {
            	Swal.fire({
    				toast: true,
    				position: 'top',
    				title: "메일 전송 실패",
    				icon: "success",
    				draggable: true,
    				showConfirmButton: false,
    				timer: 3000
    			});
            }
        })
        .catch(err => {
            console.error(err);
            Swal.fire({
				toast: true,
				position: 'top',
				title: "에러 발생",
				icon: "success",
				draggable: true,
				showConfirmButton: false,
				timer: 3000
			});
        });
    });
});
</script>