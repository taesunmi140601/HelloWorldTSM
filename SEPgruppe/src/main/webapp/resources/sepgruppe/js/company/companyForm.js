document.addEventListener("DOMContentLoaded", function () {
    const basePath = window.location.pathname.split("/").slice(0, -1).join("/");
    const elements = {
        password: document.getElementById("contactPw"),
        confirmPassword: document.getElementById("confirmPw"),
        errorMessage: document.getElementById("passwordMatchError"),
        submitBtn: document.getElementById("submitBtn"),
        contactId: document.getElementById("contactId"),
        businessRegNo: document.getElementById("businessRegNo"),
        checkBusinessDuplicateBtn: document.getElementById("checkBusinessDuplicate"),
        email: document.getElementById("contactEmail"),
        emailAuthCode: document.getElementById("authCode"),
        authResult: document.getElementById("authResult"),
        sendAuthCodeBtn: document.getElementById("sendAuthCode"),
        verifyAuthCodeBtn: document.getElementById("verifyAuthCode")
    };

    const errorElements = {
        contactId: document.getElementById("contactIdError"),
        businessRegNo: document.getElementById("businessRegNoError"),
        email: document.getElementById("emailError")
    };

    let isValidId = false;
    let isValidAuth = false;
    let isValidBusiness = false;
    let isDuplicateBusiness = true;
    let authNumber = "";

    function validateForm() {
        elements.submitBtn.disabled = !(
            elements.password.value &&
            elements.confirmPassword.value &&
            elements.password.value === elements.confirmPassword.value &&
            isValidId &&
            isValidAuth &&
            isValidBusiness &&
            !isDuplicateBusiness
        );
    }

    function validatePasswordMatch() {
        elements.errorMessage.textContent =
            elements.password.value && elements.confirmPassword.value && elements.password.value !== elements.confirmPassword.value
                ? "패스워드가 일치하지 않습니다."
                : "";
        validateForm();
    }

    function checkAvailability(url, target, successMsg, errorMsg) {
        fetch(url)
            .then(response => response.text())
            .then(isDuplicate => {
                const isValid = isDuplicate.trim() !== "true";
                target.textContent = isValid ? successMsg : errorMsg;
                target.classList.toggle("valid", isValid);
                target.classList.toggle("invalid", !isValid);

                isValidId = isValid;
                validateForm();
            })
            .catch(error => console.error(`Error checking ${url}:`, error));
    }

    elements.contactId.addEventListener("blur", function () {
        if (this.value.length < 5 || this.value.length > 15) {
            errorElements.contactId.textContent = "아이디는 5~15자 이내여야 합니다.";
            isValidId = false;
        } else {
            errorElements.contactId.textContent = "";
            checkAvailability(
                `${basePath}/checkId?contactId=${this.value}`,
                errorElements.contactId,
                "사용 가능한 아이디입니다.",
                "이미 사용 중인 아이디입니다."
            );
        }
        validateForm();
    });

    async function checkBusinessNumber(businessNumber) {
        const url = "https://api.odcloud.kr/api/nts-businessman/v1/status";
        const serviceKey = "5+AnzK0ugKGV/8twNkfnS5R1nOrVdc+k/LuzNPVwbYONycvio076NDJJBTp2Nj7SsguNsaU6m40raBNc9zQwAg==";
        const requestBody = { "b_no": [businessNumber] };

        try {
            const response = await fetch(`${url}?serviceKey=${encodeURIComponent(serviceKey)}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody)
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const data = await response.json();

            if (data?.data?.length > 0) {
                const result = data.data[0];
                isValidBusiness = result.b_stt_cd === "01";

                errorElements.businessRegNo.textContent = isValidBusiness
                    ? "유효한 사업자등록번호입니다. 중복 확인을 진행하세요."
                    : "사업자 정보를 확인할 수 없습니다.";
                errorElements.businessRegNo.style.color = isValidBusiness ? "green" : "red";

                elements.checkBusinessDuplicateBtn.disabled = !isValidBusiness;
                isDuplicateBusiness = true;
            }

        } catch (error) {
            console.error("사업자등록번호 확인 API 호출 중 오류 발생:", error);
            errorElements.businessRegNo.textContent = "API 오류 발생!";
            elements.checkBusinessDuplicateBtn.disabled = true;
            isValidBusiness = false;
        }

        validateForm();
    }

    elements.businessRegNo.addEventListener("blur", function () {
        const businessNumber = this.value.trim();
        if (businessNumber.length !== 10) {
            errorElements.businessRegNo.textContent = "사업자등록번호는 10자리여야 합니다.";
            elements.checkBusinessDuplicateBtn.disabled = true;
            isValidBusiness = false;
        } else {
            checkBusinessNumber(businessNumber);
        }
        validateForm();
    });

    elements.checkBusinessDuplicateBtn.addEventListener("click", function () {
        const businessNumber = elements.businessRegNo.value.trim();
        fetch(`${basePath}/checkBusinessDuplicate?businessRegNo=${businessNumber}`)
            .then(response => response.text())
            .then(isDuplicate => {
                isDuplicateBusiness = isDuplicate.trim() === "true";

                errorElements.businessRegNo.textContent = isDuplicateBusiness
                    ? "이미 가입된 사업자등록번호입니다."
                    : "사용 가능한 사업자등록번호입니다.";
                errorElements.businessRegNo.style.color = isDuplicateBusiness ? "red" : "green";

                validateForm();
            })
            .catch(error => console.error("사업자등록번호 중복 확인 오류:", error));
    });

    elements.sendAuthCodeBtn.addEventListener("click", function () {
        let emailValue = elements.email.value.trim();
        if (!emailValue) {
            errorElements.email.textContent = "이메일을 입력하세요.";
            return;
        }
        fetch(`${basePath}/mailAuth`, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ mail: emailValue })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    authNumber = data.number;
                    alert("인증번호가 이메일로 발송되었습니다.");
                } else {
                    alert("인증번호 발송에 실패했습니다.");
                }
            })
            .catch(error => console.error("Error sending email auth code:", error));
    });

    elements.verifyAuthCodeBtn.addEventListener("click", function () {
        let userInput = elements.emailAuthCode.value.trim();
        if (!userInput) {
            elements.authResult.textContent = "인증번호를 입력하세요.";
            isValidAuth = false;
            validateForm();
            return;
        }

        fetch(`${basePath}/mailCheck?userNumber=${encodeURIComponent(userInput)}`)
            .then(response => response.json())
            .then(isMatch => {
                isValidAuth = isMatch;
                elements.authResult.textContent = isMatch ? "인증 성공!" : "인증 실패!";
                elements.authResult.style.color = isMatch ? "green" : "red";

                validateForm();
            })
            .catch(error => console.error("Error verifying email auth code:", error));
    });

    elements.password.addEventListener("input", validatePasswordMatch);
    elements.confirmPassword.addEventListener("input", validatePasswordMatch);
});
