const loginBox = document.getElementById('loginBox');
const usernameInput = document.getElementById('email');
const passwordInput = document.getElementById('password');


function expandLogin() {
    loginBox.classList.add('active');
}


function collapseLogin() {
    if (usernameInput.value === "" && passwordInput.value === "") {
        loginBox.classList.remove('active');
    }
}

// Eventos
usernameInput.addEventListener('focus', expandLogin);
passwordInput.addEventListener('focus', expandLogin);
usernameInput.addEventListener('blur', collapseLogin);
passwordInput.addEventListener('blur', collapseLogin);