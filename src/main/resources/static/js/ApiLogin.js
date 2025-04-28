document.getElementById('login-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
     const encodeEmail = encodeURIComponent(email);
     const encodePassword = encodeURIComponent(password);
     console.log(`Encoded email: ${encodeEmail}`);
     console.log(`Encoded password: ${encodePassword}`);
     const response = await fetch(`http://localhost:8080/api/user/login/${encodeEmail}/${encodePassword}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
     });
     if (!response.ok){
         const errorText = await response.text();
         alert("Campos incorrectos");
         console.error('Error response:', errorText);
         throw new Error(errorText || 'Error al iniciar sesión');
     }
     alert("Bienvenido");
        const data = await response.json();
        console.log('Login response:', data.email);
     setTimeout(() =>{
         window.location.href = '../books.html';
     },1500);
    }catch (error) {
        console.error('Error during login:', error);
        alert('Login failed. Please try again.');
    }
});