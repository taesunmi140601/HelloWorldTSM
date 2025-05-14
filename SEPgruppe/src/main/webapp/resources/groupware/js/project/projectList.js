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
  const checkboxes = document.querySelectorAll(".projectChk");
  const deleteForm = document.getElementById("deleteForm");
  
  const currentPath = window.location.pathname;
  const tabLinks = document.querySelectorAll("#line-tab .nav-link");

//    tabLinks.forEach(link => {
//       const hrefPath = new URL(link.href).pathname;

//       if (currentPath === hrefPath || currentPath.startsWith(hrefPath)) {
//           tabLinks.forEach(l => {
//               l.classList.remove("active");
//               l.setAttribute("aria-selected", "false");
//           });

//           link.classList.add("active");
//           link.setAttribute("aria-selected", "true");
//       }
//   });

   tabLinks.forEach(link => {
           link.addEventListener("click", function (event) {
               event.preventDefault(); // 기본 링크 동작 방지

               tabLinks.forEach(tab => {
                   tab.classList.remove("active");
                   tab.setAttribute("aria-selected", "false");
               });

               link.classList.add("active");
               link.setAttribute("aria-selected", "true");

               // 해당 탭에 해당하는 내용 표시
               const targetId = link.getAttribute("href");
               const tabContents = document.querySelectorAll(".tab-pane");
               tabContents.forEach(content => {
                   content.classList.remove("show", "active");
               });
               document.querySelector(targetId).classList.add("show", "active");
           });
       });

       // 기본적으로 "내가 생성한 프로젝트" 탭 활성화
       const homeTab = document.getElementById("line-home-tab");
       homeTab.classList.add("active");
       homeTab.setAttribute("aria-selected", "true");
   
  // 전체 선택/해제
  if (checkAll) {
    checkAll.addEventListener("change", function () {
      checkboxes.forEach(chk => chk.checked = this.checked);
    });
  }

  // 개별 체크 시 전체선택 해제 처리
  checkboxes.forEach(chk => {
    chk.addEventListener("change", function () {
      if (!this.checked) {
        checkAll.checked = false;
      } else if (document.querySelectorAll(".projectChk:checked").length === checkboxes.length) {
        checkAll.checked = true;
      }
    });
  });

  // 삭제 폼 제출 시 SweetAlert2 확인창
  if (deleteForm) {
    deleteForm.addEventListener("submit", function (e) {
      e.preventDefault(); // 기본 전송 막기

      const checked = document.querySelectorAll(".projectChk:checked");
      if (checked.length === 0) {
        Swal.fire({
		  toast: true,
		  position: 'top',
          icon: "warning",
          title: "삭제할 프로젝트를 선택하세요!",
          confirmButtonColor: "#3085d6",
		  showConfirmButton: false,
	   	  timer: 2000
        });
        return false;
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
      }).then((result) => {
        if (result.isConfirmed) {
          // hidden input 생성해서 전송
          checked.forEach(chk => {
            const hidden = document.createElement("input");
            hidden.type = "hidden";
            hidden.name = "projectNos";
            hidden.value = chk.value;
            deleteForm.appendChild(hidden);
          });
          deleteForm.submit();
        }
      });
    });
  }
  
  const msgDiv = document.getElementById("delete-msg");
   if (msgDiv) {
     const successMsg = msgDiv.getAttribute("data-success");
     const failMsg = msgDiv.getAttribute("data-fail");

     if (successMsg) {
       Swal.fire({
         toast: true,
         position: 'top',
         icon: 'success',
         title: successMsg,
         showConfirmButton: false,
         timer: 2000
       });
     } else if (failMsg) {
       Swal.fire({
         toast: true,
         position: 'top',
         icon: 'error',
         title: failMsg,
         showConfirmButton: false,
         timer: 2000
       });
     }
   }
   
   
   function filterProjects(status) {
       var table, tr, td, i;
       table = document.getElementById("projectTable");
       tr = table.getElementsByTagName("tr");

       for (i = 1; i < tr.length; i++) {
           td = tr[i].getElementsByTagName("td")[6]; // 상태 컬럼
           if (td) {
               if (status === "all" || td.textContent.trim() === status) {
                   tr[i].style.display = "";
               } else {
                   tr[i].style.display = "none";
               }
           }
       }
   }
   
});

