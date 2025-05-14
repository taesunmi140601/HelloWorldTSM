/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 9.     	JSW            최초 생성
 *
 * </pre>
 */
const RECENT_MENU_KEY = 'recentMenus';
const MAX_MENUS = 5;

function addRecentMenu(menuText, menuUrl) {
  let menus = JSON.parse(localStorage.getItem(RECENT_MENU_KEY)) || [];

  // 중복 제거
  menus = menus.filter(menu => menu.url !== menuUrl);

  // 맨 앞에 추가
  menus.unshift({ text: menuText, url: menuUrl });

  // 최대 개수 초과 시 제거
  if (menus.length > MAX_MENUS) {
    menus = menus.slice(0, MAX_MENUS);
  }

  localStorage.setItem(RECENT_MENU_KEY, JSON.stringify(menus));
  renderRecentMenus();
}

function renderRecentMenus() {
  const menus = JSON.parse(localStorage.getItem(RECENT_MENU_KEY)) || [];
  const list = document.getElementById('recent-menu-list');
  if (!list) return;

  list.innerHTML = '';

  menus.forEach(menu => {
    const li = document.createElement('li');
    const a = document.createElement('a');
    a.href = menu.url;
    a.textContent = menu.text;
    li.appendChild(a);
    list.appendChild(li);
  });
}

function bindRecentMenuTracking() {
  document.querySelectorAll('.nav a[href]:not([href="#"])').forEach(anchor => {
    anchor.addEventListener('click', function () {
      const menuText = this.textContent.trim();
      const menuUrl = this.href;
      addRecentMenu(menuText, menuUrl);
    });
  });
}

// 페이지 로드 후 실행
document.addEventListener('DOMContentLoaded', function () {
  renderRecentMenus();
  bindRecentMenuTracking();
});