document.addEventListener('DOMContentLoaded', function() {



    /* myPage 값 검증 */
    $("#button").click(function(event) {
        event.preventDefault();

        const empPw = $("#empPw").val();
        const pwErrorCk = $("#empPwCk").val();
        const pwErrorSpan = $("#pwErrorCk");

        if (empPw !== pwErrorCk) {
            pwErrorSpan.text("비밀번호가 일치하지 않습니다.");
        } else {
            pwErrorSpan.text("");
        }

        /* 이메일 형식 검증 */
        const empEmail = $("input[name='empEmail']").val();
        const emailErrorSpan = $("#emailError");
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailPattern.test(empEmail)) {
            emailErrorSpan.text("유효한 이메일 형식이 아닙니다.");
        } else {
            emailErrorSpan.text("");
        }

        /* 휴대폰 번호 형식 검증 */
        const empPhone = $("input[name='empPhone']").val();
        const phoneErrorSpan = $("#phoneError");
        const phonePattern = /^\d{3}-\d{4}-\d{4}$/;

        if (!phonePattern.test(empPhone)) {
            phoneErrorSpan.text("유효한 휴대폰 번호 형식이 아닙니다. (000-0000-0000)");
        } else {
            phoneErrorSpan.text("");
        }

        /* 우편번호 필수값 검증 */
        const empZip = $("input[name='empZip']").val();
        const zipErrorSpan = $("#zipError");

        if (empZip == "") {
            zipErrorSpan.text("우편번호는 필수 입력값입니다.");
        } else {
            zipErrorSpan.text("");
        }

        /* 파일형식 검증 */
        const fileInput = $("#isFile");
        const fileErrorSpan = $("#fileError");
        const filePath = fileInput.val();
        const allowedExtensions = /(\.jpg|\.jpeg|\.png)$/i;

        if (filePath && !allowedExtensions.exec(filePath)) {
            fileErrorSpan.text("유효한 이미지 파일 형식이 아닙니다. (jpg, jpeg, png만 허용됨)");
            fileInput.val('');
        } else {
            fileErrorSpan.text("");
        }

        // 모든 검증이 통과하면 폼 제출
        if (pwErrorSpan.text() == "" && emailErrorSpan.text() == "" && phoneErrorSpan.text() == "" && zipErrorSpan.text() == "" && fileErrorSpan.text() == "") {
            $("form[name='frm'] input").each(function() {
                if ($(this).val() == "") {
                    $(this).prop('disabled', true); // 비어 있는 필드를 비활성화
                }
            });

            $("form[name='frm']").submit();
        }
    });

	// 이미지 업로드 미리보기 설정
	$("#isFile").change(function(event) {
	    const input = event.target;
	    const fileName = $('#fileName');
	    const profileImagePreview = $('#profileImagePreview');

	    if (input.files && input.files[0]) {
	        const reader = new FileReader();

	        reader.onload = function(e) {
	            profileImagePreview.attr('src', e.target.result);
	        }

	        reader.readAsDataURL(input.files[0]);
	        fileName.text(input.files[0].name);
	    }
	});

    // 이미지 삭제
	$('#deleteBtn').click(function() {
	    const $profileImagePreview = $('#profileImagePreview');
	    const $fileInput = $('.fileInput');
	    const fileName = $('#fileName');

	    // 미리보기 이미지와 파일 이름 삭제
	    $profileImagePreview.attr('src', '');
	    $fileInput.val('');
	    fileName.text('');

	    $.ajax({
	        url: '/sep/' + companyNo + '/employee/deleteImg',
	        type: 'POST',
	        data: { empId: empId },
	        success: function(resp) {
	            // 서버에서 이미지 삭제가 성공적으로 완료된 경우
	            console.log('Image deleted successfully');
	        },
	        error: function(xhr, status, error) {
	            console.error('Error deleting image:', status, error);
	        }
	    });
	});

	// 모달 Enter키 = 확인버튼
	document.getElementById('confirmPw').addEventListener('keypress', function(event) {
	    if (event.key === 'Enter') {
	        event.preventDefault(); // 기본 엔터 키 동작 방지
	        document.getElementById('verifyBtn').click(); // 확인 버튼 클릭
	    }
	});

    // 모달 띄우기
    const triggerTabList = document.querySelectorAll('#mypageBtn button');
    triggerTabList.forEach(triggerEl => {
        const tabTrigger = new bootstrap.Tab(triggerEl);

        triggerEl.addEventListener('click', event => {
            event.preventDefault();
            if (event.target.id === 'changeModal') {
                myModalAlternative.show();
            } else {
                tabTrigger.show();
            }
        });
    });

    // 모달이 열릴 때 입력 필드 초기화
    staticBackdrop.addEventListener('shown.bs.modal', event => {
        $('#confirmPw').val(''); // 입력 필드 초기화
        $('#pwErrorCk').text(''); // 에러 메시지 초기화
    });

    // 모달이 닫힐 때 입력 필드 초기화
    staticBackdrop.addEventListener('hidden.bs.modal', event => {
        $('#confirmPw').val(''); // 입력 필드 초기화
        $('#pwErrorCk').text(''); // 에러 메시지 초기화
    });

    // 마이페이지 정보수정 진입 시 비밀번호 검증
    const myModalAlternative = new bootstrap.Modal('#staticBackdrop');
    $('#verifyBtn').click(function() {
        const password = $('#confirmPw').val();

        $.ajax({
            url: '/sep/' + companyNo + '/employee/mypage/verifyPassword',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ empPw: password }),
            success: function() {
                myModalAlternative.hide();
                const triggerEl = document.querySelector('#changeModal');
                bootstrap.Tab.getInstance(triggerEl).show();

                console.log("성공");
            },
            error: function(jqXHR) {
                if (jqXHR.status === 401) {
                    Swal.fire({
                        icon: "error",
                        title: "비밀번호 인증 실패",
                        text: "비밀번호가 올바르지 않습니다!",
                        footer: '<a href="/sep/login/findPw">비밀번호 찾기</a>'
                    });
                }
            }
        });
    });
});
