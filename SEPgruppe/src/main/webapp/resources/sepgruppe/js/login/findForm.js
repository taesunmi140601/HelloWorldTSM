/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 18.     	SEM            최초 생성
 *
 * </pre>
 */

document.getElementById("findPwBtn").addEventListener("click", function () {
    const id = document.getElementById("floatingId").value.trim();
    const name = document.getElementById("floatingName").value.trim();
    const email = document.getElementById("contactEmail").value.trim();
    const resultMessage = document.getElementById("resultMessage");

    if (!name || !email) {
        resultMessage.textContent = "이름과 이메일을 입력해주세요.";
        resultMessage.style.color = "red";
        return;
    }

    fetch("/sep/login/findPw", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ contactNm: name, contactEmail: email, contactId: id })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) { 
            resultMessage.innerHTML = `
						                ✅ 찾은 아이디: <strong>${data.contactId}</strong> <br>
										<form action="/sep/login/updatePw" method="POST">
											<div class="form-floating col-6 mx-auto mb-3">
										        <input type="password" class="form-control" id="floatingPw" name="contactPw" placeholder="비밀번호">
										        <label for="floatingPw">비밀번호</label>
										    </div>
											<div class="form-floating col-6 mx-auto mb-3">
										        <input type="password" class="form-control" id="confirmPw" name="confirmPw" placeholder="비밀번호 확인">
										        <label for="confirmPw">비밀번호 확인</label>
										    </div>
											<span id="pwMismatchError" class="text-danger"></span>
											<div>
											<button type="submit" class="btn btn-success">재설정</button>
											</div>
										</form>
						            `;
            resultMessage.style.color = "green";
			
			setTimeout(addPasswordValidation, 0);
        } else {
            resultMessage.textContent = `❌ \${data.message}`;
            resultMessage.style.color = "red";
        }
    })
    .catch(error => {
        console.error("Error:", error);
        resultMessage.textContent = "⚠️ 서버 오류가 발생했습니다.";
        resultMessage.style.color = "red";
    });
});

function addPasswordValidation() {
    const pwInput = document.getElementById("floatingPw");
    const confirmPwInput = document.getElementById("confirmPw");
    const resetPwBtn = document.getElementById("resetPwBtn");
    const pwErrorMsg = document.getElementById("pwMismatchError");

    function validatePasswords() {
        if (pwInput.value !== confirmPwInput.value) {
            pwErrorMsg.textContent = "❌ 비밀번호가 일치하지 않습니다.";
            pwErrorMsg.style.color = "red";
            resetPwBtn.disabled = true;
        } else {
            pwErrorMsg.textContent = "✅ 비밀번호가 일치합니다.";
            pwErrorMsg.style.color = "green";
            resetPwBtn.disabled = false;
        }
    }

    pwInput.addEventListener("input", validatePasswords);
    confirmPwInput.addEventListener("input", validatePasswords);
}
document.addEventListener("DOMContentLoaded", function () {
    const basePath = window.location.pathname.split("/").slice(0, -1).join("/");
    const elements = {
        email: document.getElementById("contactEmail"),
        emailAuthCode: document.getElementById("authCode"),
        authResult: document.getElementById("authResult"),
        sendAuthCodeBtn: document.getElementById("sendAuthCode"),
        verifyAuthCodeBtn: document.getElementById("verifyAuthCode"),
		findIdBtn: document.getElementById("findPwBtn"),
    };

    const errorElements = {
        email: document.getElementById("emailError"),
    };

    let isValidAuth = false;
    let authNumber = "";

    // ✅ 이메일 인증번호 발송
    elements.sendAuthCodeBtn.addEventListener("click", function () {
        let emailValue = elements.email.value.trim();
        if (!emailValue) {
            errorElements.email.textContent = "이메일을 입력하세요.";
            return;
        }
        fetch(`/sep/company/mailAuth`, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ mail: emailValue }),
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.success) {
                    authNumber = data.number;
                    alert("📩 인증번호가 이메일로 발송되었습니다.");
                } else {
                    alert("⚠️ 인증번호 발송에 실패했습니다.");
                }
            })
            .catch((error) => console.error("Error sending email auth code:", error));
    });

    // ✅ 이메일 인증번호 확인
    elements.verifyAuthCodeBtn.addEventListener("click", function () {
        let userInput = elements.emailAuthCode.value.trim();
        if (!userInput) {
            elements.authResult.textContent = "인증번호를 입력하세요.";
            isValidAuth = false;
            updateFindIdBtnState();
            return;
        }

        fetch(`/sep/company/mailCheck?userNumber=${encodeURIComponent(userInput)}`)
            .then((response) => response.json())
            .then((isMatch) => {
                isValidAuth = isMatch;
                elements.authResult.textContent = isMatch ? "✅ 인증 성공!" : "❌ 인증 실패!";
                elements.authResult.style.color = isMatch ? "green" : "red";

                updateFindIdBtnState();
            })
            .catch((error) => console.error("Error verifying email auth code:", error));
    });
	function updateFindIdBtnState() {
	       elements.findIdBtn.disabled = !isValidAuth;
	   }

	   updateFindIdBtnState();
});