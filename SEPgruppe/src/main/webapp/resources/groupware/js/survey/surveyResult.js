/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 26.     	SJH            최초 생성 
 * 2025. 3. 27.     	SJH            최종?
 *
 * </pre>
 */
 
 
// 전역 변수: 설문 정보 전체를 저장
let surveyData = null;

// 전역 변수: 현재 선택된 차트 타입 (기본값은 'pie')
let chartType = "pie";

// ✅ [1] 페이지 로딩 완료 후 초기 세팅
document.addEventListener("DOMContentLoaded", () => {

    // 👉 차트 타입 드롭다운 선택시 chartType 변경 & 다시 그리기
//    document.getElementById("chartTypeSelect").addEventListener("change", e => {
//        chartType = e.target.value;        // 선택한 차트 타입 저장
//        fetchSurveyResults();              // 다시 그리기
//    });

    // 👉 설문 ID가 존재하면 설문 정보 & 응답 데이터 로딩
    if (surveyId) {
        fetchSurveyInfo();
        fetchSurveyResults();
    } else {
        alert("잘못된 접근입니다.");
        window.location.href = `${contextPath}/${companyNo}/survey`;
    }
});

// ✅ [2] 설문 기본 정보 (제목, 설명 등) 가져오기
async function fetchSurveyInfo() {
    try {
        const response = await fetch(`${contextPath}/${companyNo}/surveyApi/surveys/${surveyId}/details/data`);
        if (!response.ok) throw new Error(`설문 정보 요청 실패: ${response.status}`);
        surveyData = await response.json();

        // ✅ 제목 처리
        document.getElementById("surveyTitle").textContent = surveyData.title || "제목 없음";
		
		// ✅ 설명 처리 (페이지 내부에 있을 수 있음!)
        const rawDescription = (
            surveyData.description || 
            surveyData.pages?.[0]?.description || 
            ""
        ).trim();
        document.getElementById("surveyDescription").textContent = rawDescription.length > 0
            ? rawDescription
            : "(설명 없음)";
			
    } catch (error) {
        console.error("설문 정보 가져오기 실패:", error);
    }
}

// ✅ [3] 설문 응답 데이터 가져오기
async function fetchSurveyResults() {
    try {
        const response = await fetch(`${contextPath}/${companyNo}/surveyApi/surveys/${surveyId}/responses`);
        if (!response.ok) throw new Error(`API 요청 실패: ${response.status}`);
        const data = await response.json();

        processSurveyResults(data); // 응답 데이터 처리
    } catch (error) {
        console.error("설문 결과 가져오기 실패:", error);
		document.getElementById("surveyDescription").textContent = "설문 데이터를 불러오는 중 오류가 발생했습니다.";
    }
}

// ✅ [4] 응답 데이터를 객관식 / 주관식으로 분리해서 집계
function processSurveyResults(data) {
    const questionStats = {};
    const openAnswers = {};

    data.data.forEach(response => {
        response.pages.forEach(page => {
            page.questions.forEach(question => {
                const questionId = question.id;

                // ✅ 질문 제목 정확하게 가져오기 (수정됨)
                const original = surveyData.pages.flatMap(p => p.questions).find(q => q.id === questionId);
                const questionText = original?.headings?.[0]?.heading?.trim() || "(제목 없음)";

                const answers = question.answers;
                const isOpenEnded = !original?.answers?.choices;

                if (isOpenEnded) {
                    if (!openAnswers[questionId]) {
                        openAnswers[questionId] = { text: questionText, values: [] };
                    }
                    answers?.forEach(ans => {
                        openAnswers[questionId].values.push(ans.text || "미입력");
                    });
                } else {
                    if (!questionStats[questionId]) {
                        questionStats[questionId] = { text: questionText, counts: {} };
                    }
                    answers?.forEach(ans => {
                        const choiceText = findChoiceText(questionId, ans.choice_id) || "기타";
                        questionStats[questionId].counts[choiceText] = (questionStats[questionId].counts[choiceText] || 0) + 1;
                    });
                }
            });
        });
    });

    renderQuestions(questionStats, openAnswers);
}


// ✅ [5] 객관식 문항의 선택지 ID → 실제 텍스트 변환
function findChoiceText(questionId, choiceId) {
    const question = surveyData.pages.flatMap(p => p.questions).find(q => q.id === questionId);
    const choice = question?.answers?.choices?.find(c => c.id === choiceId);
    return choice?.text || null;
}

// ✅ [6] 화면에 질문 + 응답 결과 렌더링
function renderQuestions(stats, openAnswers) {
    const questionList = document.getElementById("questionList");
    questionList.innerHTML = ""; // 초기화

    // ✅ 객관식 질문 + 차트 렌더링
    Object.entries(stats).forEach(([questionId, question]) => {
        const container = document.createElement("div");
        container.classList.add("question-block");

        // ✅ 질문 제목 추가
        const title = document.createElement("h4");
        title.textContent = question.text;
        container.appendChild(title);

        // ✅ 차트 캔버스 추가
        const canvas = document.createElement("canvas");
        canvas.id = `chart_${questionId}`;
        canvas.style.maxWidth = "400px";
        canvas.style.maxHeight = "400px";
        container.appendChild(canvas);

        // ✅ DOM에 붙이기
        questionList.appendChild(container);

        // ✅ 차트 그리기
        const labels = Object.keys(question.counts);
        const data = Object.values(question.counts);

        new Chart(canvas, {
            type: chartType,
            data: {
                labels,
                datasets: [{
                    label: "응답 수",
                    data,
                    backgroundColor: ["#3498db", "#2ecc71", "#f1c40f", "#e74c3c", "#9b59b6"]
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { position: "bottom" }
                }
            }
        });
    });

    // ✅ 주관식 질문은 그대로 유지
    Object.values(openAnswers).forEach(q => {
        const container = document.createElement("div");
        container.classList.add("question-block");
        container.innerHTML = `<h4>${q.text}</h4><ul>${q.values.map(ans => `<li>${ans}</li>`).join("")}</ul>`;
        questionList.appendChild(container);
    });
}



document.addEventListener("DOMContentLoaded", () => {
    const lotties = document.querySelectorAll('.lottie-clickable');
    const chartContainer = document.getElementById("chartContainer");

    lotties.forEach(lottie => {
        // 마우스 올리면 정지
        lottie.addEventListener("mouseenter", () => {
            lottie.pause();
			lottie.classList.add("grayscale");
        });

        // 마우스 벗어나면 다시 재생
        lottie.addEventListener("mouseleave", () => {
            lottie.play();
			lottie.classList.remove("grayscale");
        });

        // 클릭하면 결과 표시
        lottie.addEventListener("click", () => {
            chartContainer.scrollIntoView({ behavior: "smooth" });
        });
    });
});

document.addEventListener("DOMContentLoaded", () => {
    // 기존 드롭다운 제거 → 대신 Lottie 클릭
    document.querySelectorAll("lottie-player").forEach(player => {
        const type = player.getAttribute("data-type");

        // 마우스를 올리면 애니메이션 멈춤
        player.addEventListener("mouseenter", () => {
            player.pause();
        });

        // 마우스가 나가면 다시 재생
        player.addEventListener("mouseleave", () => {
            player.play();
        });

        // 클릭 시 해당 차트 타입으로 변경 후 재요청
        player.addEventListener("click", () => {
            chartType = type;
            fetchSurveyResults();
        });
    });

    // 설문 ID 존재 시 정보 불러오기
    if (surveyId) {
        fetchSurveyInfo();
        fetchSurveyResults();
    } else {
        alert("잘못된 접근입니다.");
        window.location.href = `${contextPath}/${companyNo}/survey`;
    }
});

document.querySelectorAll(".lottie-clickable").forEach(player => {
    player.addEventListener("click", () => {
        chartType = player.dataset.type;
        fetchSurveyResults(); // 차트 새로 그리기
    });
});
