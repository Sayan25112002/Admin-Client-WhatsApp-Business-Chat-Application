const API_BASE_URL = "http://localhost:8080";


const loginForm = document.getElementById("loginForm");

const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");

const captchaIdInput = document.getElementById("captchaId");
const captchaAnswerInput = document.getElementById("captchaAnswer");
const captchaImage = document.getElementById("captchaImage");
const captchaLoading = document.getElementById("captchaLoading");

const emailError = document.getElementById("emailError");
const passwordError = document.getElementById("passwordError");
const captchaError = document.getElementById("captchaError");

const message = document.getElementById("message");

const loginButton = document.getElementById("loginButton");
const buttonText = document.getElementById("buttonText");


/*
 * Show message
 */
function showMessage(text, type) {

    message.textContent = text;

    message.className = `message ${type}`;
}


/*
 * Clear errors
 */
function clearErrors() {

    emailError.textContent = "";
    passwordError.textContent = "";
    captchaError.textContent = "";

}


/*
 * Load CAPTCHA
 */
async function loadCaptcha() {

    captchaImage.style.visibility = "hidden";

    captchaLoading.style.display = "block";

    captchaLoading.textContent = "Loading...";

    captchaAnswerInput.value = "";

    captchaError.textContent = "";


    try {

        const response = await fetch(
            `${API_BASE_URL}/generateImageCaptcha`
        );


        if (!response.ok) {

            throw new Error(
                "Unable to generate CAPTCHA."
            );

        }


        const data = await response.json();


        /*
         * Your CaptchaResponseDto must contain:
         *
         * captchaId
         * captchaImage
         */

        captchaIdInput.value = data.captchaId;

        captchaImage.src = data.captchaImage;


        captchaImage.onload = () => {

            captchaLoading.style.display = "none";

            captchaImage.style.visibility = "visible";

        };


    } catch (error) {

        captchaLoading.textContent =
            "CAPTCHA unavailable";

        showMessage(
            error.message,
            "error"
        );

    }

}


/*
 * Validate login form
 */
function validateForm() {

    clearErrors();

    let valid = true;


    if (!emailInput.value.trim()) {

        emailError.textContent =
            "Email is required.";

        valid = false;

    }


    if (!passwordInput.value) {

        passwordError.textContent =
            "Password is required.";

        valid = false;

    }


    if (!captchaIdInput.value) {

        captchaError.textContent =
            "Please refresh CAPTCHA.";

        valid = false;

    }


    if (!captchaAnswerInput.value.trim()) {

        captchaError.textContent =
            "CAPTCHA answer is required.";

        valid = false;

    }


    return valid;

}


/*
 * Show / Hide Password
 */
document
    .getElementById("togglePassword")
    .addEventListener("click", () => {

        const button =
            document.getElementById("togglePassword");


        if (passwordInput.type === "password") {

            passwordInput.type = "text";

            button.textContent = "Hide";

        } else {

            passwordInput.type = "password";

            button.textContent = "Show";

        }

    });


/*
 * Refresh CAPTCHA
 */
document
    .getElementById("refreshCaptcha")
    .addEventListener(
        "click",
        loadCaptcha
    );


/*
 * LOGIN
 */
loginForm.addEventListener(
    "submit",
    async (event) => {

        event.preventDefault();


        message.className = "message";

        message.textContent = "";


        if (!validateForm()) {

            return;

        }


        loginButton.disabled = true;

        loginButton.classList.add("loading");

        buttonText.textContent = "Logging in...";


        const loginRequest = {

            email: emailInput.value.trim(),

            password: passwordInput.value,

            captchaId: captchaIdInput.value,

            captchaAnswer:
                captchaAnswerInput.value.trim()

        };


        try {

            const response = await fetch(
                `${API_BASE_URL}/auth/login`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(loginRequest)
                }
            );


            let data = {};


            try {

                data = await response.json();

            } catch (_) {

                /*
                 * Empty response body.
                 */

            }


            if (!response.ok) {

                throw new Error(
                    data.message ||
                    data.error ||
                    `Login failed (${response.status})`
                );

            }


            /*
             * LoginResponseDto
             *
             * {
             *   message,
             *   tokenType,
             *   accessToken,
             *   refreshToken,
             *   verified,
             *   userId,
             *   name,
             *   email,
             *   contact,
             *   role,
             *   userStatus
             * }
             */


            localStorage.setItem(
                "accessToken",
                data.accessToken
            );


            localStorage.setItem(
                "refreshToken",
                data.refreshToken
            );


            localStorage.setItem(
                "userId",
                data.userId
            );


            localStorage.setItem(
                "userName",
                data.name
            );


            localStorage.setItem(
                "userEmail",
                data.email
            );


            localStorage.setItem(
                "userRole",
                data.role
            );


            localStorage.setItem(
                "userStatus",
                data.userStatus
            );


            showMessage(
                data.message ||
                "Login successful.",
                "success"
            );


            /*
             * Change this later to your
             * actual WhatsApp chat page.
             */

            setTimeout(() => {

                window.location.href =
                    "chat.html";

            }, 700);


        } catch (error) {

            showMessage(
                error.message ||
                "Login failed.",
                "error"
            );


            /*
             * Your CAPTCHA is consumed
             * during login, so get a new one.
             */

            await loadCaptcha();


        } finally {

            loginButton.disabled = false;

            loginButton.classList.remove(
                "loading"
            );

            buttonText.textContent = "Login";

        }

    }
);


/*
 * Initial CAPTCHA
 */
loadCaptcha();