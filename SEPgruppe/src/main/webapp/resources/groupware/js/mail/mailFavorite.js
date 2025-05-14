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
    const FAVORITE_KEY = "favoriteMails";

    function getFavorites() {
        return JSON.parse(localStorage.getItem(FAVORITE_KEY)) || [];
    }

    document.addEventListener("DOMContentLoaded", () => {
        const favorites = getFavorites();
		const favoriteMailListBody = document.getElementById("favoriteBody");

		    if (allMails && allMails.length > 0) { // allMails가 있는지 확인
		        const favoriteMails = allMails.filter(mail => mail.mailFav === 1); // MAIL_FAV가 1인 메일만 필터링
				
			if (favoriteMails.length > 0) {
	            favoriteMails.forEach(mail => {
	                const row = favoriteMailListBody.insertRow();
					row.innerHTML = `
					    <td><input type="checkbox" name="deleteMail"></td>
					    <td><i class="fa-solid fa-star favorite-icon" data-mail-id="${mail.mailId}" style="color: gold; cursor: pointer;"></i></td>
					    <td class="mail-from">${mail.fromEmail}</td>
					    <td class="mail-subject"><a href="/${companyNo}/mail/mailContent/${mail.mailId}">${mail.mailSubject}</a></td>
					    <td class="mail-date">${mail.mailDate}</td>
					`;


				});
			} else {
			document.querySelector(".no-mail").textContent = "⭐ 즐겨찾기된 메일이 없습니다.";
			}
		} else {
		document.querySelector(".no-mail").textContent = "⭐ 메일 목록을 불러오는 중 오류가 발생했습니다.";
		}
	});