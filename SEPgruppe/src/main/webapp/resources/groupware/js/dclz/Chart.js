/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 24.     	JSW            최초 생성
 *
 * </pre>
 */

function renderBarChart() {
    const ctx = document.getElementById('barChart').getContext('2d');

    if (window.barChartInstance) {
        window.barChartInstance.destroy();
    }

    const weeks = weeklyWorkData.map(data => `Week ${data.week}`);
    const hours = weeklyWorkData.map(data => data.hours);

    window.barChartInstance = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: weeks,
            datasets: [{
                label: '주간 근무시간 (시간)',
                data: hours,
                backgroundColor: 'rgba(54, 162, 235, 0.6)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
				y: {
				   min: 0,
				   ticks: {
				     beginAtZero: true,
				     stepSize: 1,
				     callback: function(value) {
				       return value.toFixed(0); 
				     }
				   },
				   title: {
				     display: true,
				     text: '시간'
                    }
                }
            }
        }
    });
}

// 페이지 로드 후 차트 렌더링
$(document).ready(function () {
    renderBarChart();
});