/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 26.     	KKM            최초 생성
 *
 * </pre>
 */
function initializeGantt(gantTaskList) {
		    const container = document.getElementById("gantt");
		    if (!container) {
		        console.error("⛔ 'gantt' 엘리먼트가 존재하지 않음");
		        return;
		    }
		
		    const validTasks = gantTaskList.map(task => {
		        const start = new Date(task.start_date);
		        const end = new Date(task.end_date);
		        return {
		            id: task.id,
		            name: task.name,
		            start: start,
		            end: end,
		            progress: Number(task.progress) || 0
		        };
		    });
		
		    const gantt = new Gantt(container, validTasks, {
		        view_mode: "Day",
		        bar_height: 24,
		        padding: 20,
		        on_click: (task) => {
		            console.log("🖱️ 클릭:", task);
		        },
		        custom_popup_html: (task) => `
		            <div class="gantt-popup">
		                <h5>${task.name}</h5>
		                <p>시작일: ${task.start.toLocaleDateString()}</p>
		                <p>종료일: ${task.end.toLocaleDateString()}</p>
		                <p>진행률: ${task.progress}%</p>
		            </div>
		        `
		    });
		}