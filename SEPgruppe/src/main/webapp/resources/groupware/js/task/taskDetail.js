function showToast(message, icon = 'success') {
    Swal.fire({
        toast: true,
        position: 'top',
        icon: icon,
        title: message,
        showConfirmButton: false,
        timer: 2000,
        timerProgressBar: true
    });
}

// 삭제 확인창 띄우기
function confirmDelete(taskNo, projectNo, companyNo, csrfName, csrfToken) {
    Swal.fire({
        title: '삭제하시겠습니까?',
        text: '이 작업은 되돌릴 수 없습니다.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#aaa',
        confirmButtonText: '삭제',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            // form 동적으로 생성해서 submit
            const form = document.createElement('form');
            form.method = 'post';
            form.action = contextPath +`/${companyNo}/task/${taskNo}/delete`;

            // method override
            const methodInput = document.createElement('input');
            methodInput.type = 'hidden';
            methodInput.name = '_method';
            methodInput.value = 'delete';
            form.appendChild(methodInput);

            // projectNo
            const projectInput = document.createElement('input');
            projectInput.type = 'hidden';
            projectInput.name = 'projectNo';
            projectInput.value = projectNo;
            form.appendChild(projectInput);

            // CSRF
            const csrfInput = document.createElement('input');
            csrfInput.type = 'hidden';
            csrfInput.name = csrfName;
            csrfInput.value = csrfToken;
            form.appendChild(csrfInput);

            document.body.appendChild(form);
            form.submit();
        }
    });
}

// 페이지 로딩 후 deleteSuccess 메시지 있으면 Toast 띄우기
document.addEventListener('DOMContentLoaded', function () {
    const deleteMsg = document.getElementById('delete-msg');
    if (deleteMsg && deleteMsg.dataset.success === 'true') {
        showToast('삭제가 완료되었습니다.', 'success');
    }
});