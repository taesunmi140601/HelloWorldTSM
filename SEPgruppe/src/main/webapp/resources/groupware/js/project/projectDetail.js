/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 3.     	JSW            최초 생성
 *
 * </pre>
 */
document.addEventListener("DOMContentLoaded", function () {
  const statusSelect = document.getElementById("statusSelect");
  const msgSpan = document.getElementById("statusUpdateMsg");
  
  const currentPath = window.location.pathname;
  const tabLinks = document.querySelectorAll("#line-tab .nav-link");

  tabLinks.forEach(link => {
     const hrefPath = new URL(link.href).pathname;

     if (currentPath === hrefPath || currentPath.startsWith(hrefPath)) {
         tabLinks.forEach(l => {
             l.classList.remove("active");
             l.setAttribute("aria-selected", "false");
         });

         link.classList.add("active");
         link.setAttribute("aria-selected", "true");
     }
 });

  if (statusSelect) {
    statusSelect.addEventListener("change", function () {
      const projectNo = this.dataset.projectNo;
      const companyNo = this.dataset.companyNo;
      const selectedStatus = this.value;

      fetch(contextPath +`/${companyNo}/project/${projectNo}/status`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'X-Requested-With': 'XMLHttpRequest'
        },
        body: JSON.stringify({ projectStatus: selectedStatus })
      })
        .then(response => response.json())
        .then(result => {
          if (result.success) {
			Swal.fire({
		         toast: true,
		         position: 'top',
		         icon: 'success',
		         title: '상태 변경 완료',
		         showConfirmButton: false,
		         timer: 2000
			 });
          } else {
			Swal.fire({
		         toast: true,
		         position: 'top',
		         icon: 'error',
		         title: '상태 변경 실패',
		         showConfirmButton: false,
		         timer: 2000
	       });
          }
        })
        .catch(error => {
			Swal.fire({
		         toast: true,
		         position: 'top',
		         icon: 'error',
		         title: '오류 발생 관리자에게 문의 하세요.',
		         showConfirmButton: false,
		         timer: 2000
	       });
        });
    });
  }
});