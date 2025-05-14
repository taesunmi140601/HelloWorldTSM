/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 7.     	KMJ            최초 생성
 *
 * </pre>
 */

document.addEventListener("DOMContentLoaded", () => {

    // ✅ 1. 즐겨찾기 아이콘 표시 처리
    fetchFavoriteMailIds(companyNo, empId).then(favoriteMailIds => {
        updateFavoriteIcons(favoriteMailIds);
    });

    document.querySelectorAll(".favorite-icon").forEach(icon => {
        icon.addEventListener("click", () => {
            const mailId = icon.dataset.mailId;
            const isFavorite = icon.classList.contains("fa-solid");

            if (isFavorite) {
                removeFavoriteFromServer(companyNo, mailId).then(() => {
                    icon.classList.remove("fa-solid");
                    icon.classList.add("fa-regular");
                    icon.style.color = "";
                });
            } else {
                addFavoriteToServer(companyNo, mailId).then(() => {
                    icon.classList.remove("fa-regular");
                    icon.classList.add("fa-solid");
                    icon.style.color = "gold";
                });
            }
        });
    });

    // ✅ 2. 즐겨찾기 목록 출력 (이 부분 추가된 거!)
    const favoriteListContainer = document.getElementById("favoriteList"); // ★ JSP에 <div id="favoriteList"></div> 있어야 함

    if (favoriteListContainer) {
        fetch(`${contextPath}/${companyNo}/mail/favorites/list`)
            .then(response => response.json())
            .then(data => {
                if (data.length === 0) {
                    favoriteListContainer.innerHTML = "<p>즐겨찾기한 메일이 없습니다.</p>";
                    return;
                }

                let html = "";
                data.forEach(mail => {
                    html += `
                        <div class="mail-item">
                            <span class="favorite-icon fa-solid fa-star" style="color: gold;" data-mail-id="${mail.mailId}"></span>
                            <strong>${mail.mailSubject}</strong>
                            <span> - ${mail.fromEmail}</span>
                            <span> (${mail.mailDate})</span>
                        </div>
                    `;
                });

                favoriteListContainer.innerHTML = html;
            })
            .catch(error => {
                console.error("즐겨찾기 메일 조회 실패:", error);
            });
    }

    // ✅ 3. 즐겨찾기된 메일 ID 목록 가져오기
    function fetchFavoriteMailIds(companyNo, empId) {
        return fetch(contextPath + "/" + companyNo + "/mail/favorites", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => data)
        .catch(error => console.error('Error fetching favorites:', error));
    }

    // ✅ 4. 아이콘 업데이트
    function updateFavoriteIcons(favoriteMailIds) {
        document.querySelectorAll(".favorite-icon").forEach(icon => {
            const mailId = icon.dataset.mailId;
            if (favoriteMailIds && favoriteMailIds.includes(mailId)) {
                icon.classList.remove("fa-regular");
                icon.classList.add("fa-solid");
                icon.style.color = "gold";
            } else {
                icon.classList.remove("fa-solid");
                icon.classList.add("fa-regular");
                icon.style.color = "";
            }
        });
    }

    // ✅ 5. 즐겨찾기 추가
    function addFavoriteToServer(companyNo, mailId) {
        return fetch(contextPath + "/" + companyNo + "/mail/favorites/add?mailId=" + `${mailId}`, {
            method: 'POST'
        })
        .then(response => response.text())
        .then(data => {
            if (data !== "success") {
                console.error("즐겨찾기 추가 실패");
            }
        })
        .catch(error => console.error('추가 실패:', error));
    }

    // ✅ 6. 즐겨찾기 제거
    function removeFavoriteFromServer(companyNo, mailId) {
        return fetch(contextPath + "/" + companyNo + "/mail/favorites/remove?mailId=" + `${mailId}`, {
            method: 'POST'
        })
        .then(response => response.text())
        .then(data => {
            if (data !== "success") {
                console.error("즐겨찾기 제거 실패");
            }
        })
        .catch(error => console.error('삭제 실패:', error));
    }
	
	$(document).ready(function() {
	    $('#writeMailBtn').on('click', function(e) {
	        e.preventDefault();
	        $.ajax({
	            url: contextPath + "/" + companyNo + "/mail/send", // JSP 페이지 반환하는 URL
	            type: "GET",
	            success: function(data) {
	                $('.content-area').html(data); // 메일 리스트 영역을 폼으로 대체
	            },
	            error: function() {
					Swal.fire({
						toast: true,
						position: 'top',
						title: "메일 작성 화면 로딩 실패",
						icon: "success",
						draggable: true,
						showConfirmButton: false,
						timer: 2000
					});
	            }
	        });
	    });
	});
	
	$(document).on('click', '.mail-link', function () {
	    const mailId = $(this).data('mail-id');
	    const type = $(this).data('type'); // inbox 또는 sent

	    // URL 동적으로 생성
	    const url = type === 'sent'
	        ? `${contextPath}/${companyNo}/mail/sentMailContent/${mailId}`
	        : `${contextPath}/${companyNo}/mail/mailContent/${mailId}`;

	    $.ajax({
	        url: url,
	        method: 'GET',
	        success: function (html) {
	            $('.content-area').html(html); // 새 HTML로 교체
	        },
	        error: function () {
				Swal.fire({
					toast: true,
					position: 'top',
					title: "메일 내용을 불러오는 데 실패했습니다.",
					icon: "success",
					draggable: true,
					showConfirmButton: false,
					timer: 2000
				});
	        }
	    });
	});
	
	$(function () {
	  $('.mailSidebar .menu-item').on('click', function (e) {
	    e.preventDefault();
	    const url = $(this).data('url');

	    $.ajax({
	      url: contextPath + url,
	      type: 'GET',
	      success: function (data) {
	        $('.content-area').html(data); // 받은 HTML을 mail 리스트 영역에 삽입
	      },
	      error: function () {
			  Swal.fire({
				  toast: true,
				  position: 'top',
				  title: "메일 로딩 중 오류가 발생했습니다.",
				  icon: "success",
				  draggable: true,
				  showConfirmButton: false,
				  timer: 2000
			  });
	      }
	    });
	  });
	});
	
	$(document).on('click', '.mail-back-btn', function (e) {
	  e.preventDefault();
	  const url = $(this).data('url');

	  $.ajax({
	    url: contextPath + url,
	    type: 'GET',
	    success: function (data) {
	      $('.content-area').html(data); // 메일 리스트 다시 로드
	    },
	    error: function () {
			Swal.fire({
				toast: true,
				position: 'top',
				title: "받은 메일함을 불러오는 중 오류 발생.",
				icon: "success",
				draggable: true,
				showConfirmButton: false,
				timer: 2000
			});
	    }
	  });
	});
});
