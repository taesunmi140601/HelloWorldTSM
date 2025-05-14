/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 26.     	SJH            최초 생성
 *
 * </pre>
 */
if (!surveyId) {
      alert("잘못된 접근입니다.");
      window.location.href = contextPath + "/" + companyNo + "/survey";
    }

    let surveyData = null;

	// 설문 상세 정보 조회
    async function fetchSurveyDetails() {
      try {
		const response = await fetch(`${contextPath}/${companyNo}/surveyApi/surveys/${surveyId}/details/data`);
        if (!response.ok) throw new Error("API 요청 실패");
        surveyData = await response.json();
        displaySurveyDetails(surveyData);
      } catch (error) {
        alert("설문을 불러오는 중 오류가 발생했습니다.");
      }
    }

	// 설문 화면 표시
    function displaySurveyDetails(survey) {
      document.getElementById("surveyTitle").textContent = survey.title;
      const descriptionElement = document.getElementById("surveyDescription");
      const surveyDesc = survey.description || (survey.pages.length > 0 ? survey.pages[0].description : "설명 없음");
      descriptionElement.textContent = surveyDesc || "설명 없음";
      descriptionElement.style.display = "block";

      const questionList = document.getElementById("questionList");
      questionList.innerHTML = survey.pages[0].questions.map(q => {
        const optionsHtml = getQuestionOptionsHtml(q);
        return `
          <div class="question">
            <strong>${q.headings[0].heading}</strong>
            ${optionsHtml}
          </div>
        `;
      }).join("");
    }

	// 질문 유형별 HTML 생성
    function getQuestionOptionsHtml(question) {
      if (!question.answers) return createTextareaHtml(question.id);
      return question.family === 'multiple_choice' ?
        createOptionsHtml(question, "checkbox") :
        createOptionsHtml(question, "radio");
    }

	// HTML 생성 헬퍼 함수들
    function createTextareaHtml(id) {
      return `<div class="options"><textarea class="open-answer" name="q_${id}" rows="3"></textarea></div>`;
    }

	// 체크박스 & 라디오 버튼
    function createOptionsHtml(question, type) {
      return `<div class="options">${question.answers.choices.map(choice => `
        <div class="option">
          <input type="${type}" name="q_${question.id}" value="${choice.text}">
          <label>${choice.text}</label>
        </div>
      `).join('')}</div>`;
    }

	// 응답 데이터 수집
    function collectAnswers() {
      return [...document.querySelectorAll('.question')].map((question, index) => {
        const questionId = surveyData.pages[0].questions[index].id;
        const answer = getQuestionAnswer(question);
        return answer ? { question: questionId, answer } : null;
      }).filter(Boolean);
    }

	// 질문별 답변 수집
    function getQuestionAnswer(question) {
      const selected = question.querySelectorAll('input:checked');
      if (selected.length) return [...selected].map(input => input.value);
      const textarea = question.querySelector('textarea');
      return textarea?.value || null;
    }

	// 응답 제출 처리
    document.getElementById("endSurveyBtn").addEventListener("click", async () => {
      try {
        const answers = collectAnswers();
        if (!answers.length) {
          alert("최소 하나 이상의 문항에 응답해야 합니다.");
          return;
        }

        const collectorId = await getOrCreateCollector();
        if (!collectorId) {
          alert("설문 collector 정보를 찾을 수 없습니다.");
          return;
        }

        const response = await submitSurveyResponse(answers, collectorId);
        if (!response.ok) {
          const errorData = await response.text();
          throw new Error(`응답 제출 실패: ${errorText}`);
        }
		
		const data = await response.json();

        alert("응답이 성공적으로 저장되었습니다! 설문 리스트로 이동합니다.");
        setTimeout(() => location.href = `${contextPath}/${companyNo}/surveyApi/list`, 1500);
      } catch (error) {
        alert(`응답 제출 중 오류가 발생했습니다: ${error.message}`);
      }
    });

	// Collector 생성 함수 추가 및 getCollectorId 함수 수정
    async function getOrCreateCollector() {
      try {
        const res = await fetch(`${contextPath}/${companyNo}/surveyApi/collectors/${surveyId}`);
        if (!res.ok) throw new Error("콜렉터 호출 실패");
        const data = await res.json();
        return data.collector_id;
      } catch (e) {
        console.error("Collector Error:", e);
        throw e;
      }
    }

	// API 응답 제출
    async function submitSurveyResponse(answers, collectorId) {
      const requestData = {
        collector_id: collectorId,
        pages: [{
          id: surveyData.pages[0].id,
          questions: answers.map(ans => formatQuestionResponse(ans))
        }]
      };

      return fetch(`${contextPath}/${companyNo}/surveyApi/collectors/${collectorId}/responses`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(requestData)
      });
    }

	// 응답 데이터 포맷팅
    function formatQuestionResponse(ans) {
      const question = surveyData.pages[0].questions.find(q => q.id === ans.question);
      if (question?.family === 'single_choice' || question?.family === 'multiple_choice') {
        return {
          id: ans.question,
          answers: (Array.isArray(ans.answer) ? ans.answer : [ans.answer]).map(choice => ({
            choice_id: question.answers.choices.find(c => c.text === choice)?.id
          }))
        };
      }
      return {
        id: ans.question,
        answers: [{ text: ans.answer }]
      };
    }

	// 초기 로드
    fetchSurveyDetails();