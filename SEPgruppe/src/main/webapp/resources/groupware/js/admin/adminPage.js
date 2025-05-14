/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 14.     	KMJ            최초 생성
 *
 * </pre>
 */

/*나중에 여기 지우기 (차트)*/

document.addEventListener("DOMContentLoaded", function () {
var lineChart = document.getElementById("lineChart").getContext("2d")

var myLineChart = new Chart(lineChart, {
        type: "line",
        data: {
          labels: [
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec",
          ],
          datasets: [
            {
              label: "Active Users",
              borderColor: "#1d7af3",
              pointBorderColor: "#FFF",
              pointBackgroundColor: "#1d7af3",
              pointBorderWidth: 2,
              pointHoverRadius: 4,
              pointHoverBorderWidth: 1,
              pointRadius: 4,
              backgroundColor: "transparent",
              fill: true,
              borderWidth: 2,
              data: [
                542, 480, 430, 550, 530, 453, 380, 434, 568, 610, 700, 900,
              ],
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          legend: {
            position: "bottom",
            labels: {
              padding: 10,
              fontColor: "#1d7af3",
            },
          },
          tooltips: {
            bodySpacing: 4,
            mode: "nearest",
            intersect: 0,
            position: "nearest",
            xPadding: 10,
            yPadding: 10,
            caretPadding: 10,
          },
          layout: {
            padding: { left: 15, right: 15, top: 15, bottom: 15 },
          },
        },
      });
	});	  
	
/*여기까지 지우기*/	

/* 토글 이벤트 */	
document.querySelectorAll('.menu-title').forEach(item => {
    item.addEventListener('click', () => {
        const submenu = item.querySelector('.submenu');
        const icon = item.querySelector('i');

        if (submenu.style.display === 'block') { // 토글 열려있으면
            submenu.style.display = 'none'; 
            icon.classList.remove('fa-angle-double-down'); // 오른쪽화살표아이콘임
            icon.classList.add('fa-angle-double-right');   // 토글 닫히면 아래화살표아이콘임
        } else {
            submenu.style.display = 'block';
            icon.classList.remove('fa-angle-double-right');
            icon.classList.add('fa-angle-double-down');
        }
    });
});
	