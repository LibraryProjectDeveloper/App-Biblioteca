document.addEventListener('DOMContentLoaded', function () {
    const saveBookBtn = document.getElementById('saveBook');
    saveBookBtn.addEventListener('click',save);
    loadBooks();
});

let curretentBookId = null;

function loadBooks() {
    fetch('http://localhost:8080/api/books/')
        .then(response => response.json())
        .then(data => {
            console.log('Books data:', data);
            const booksTable = document.querySelector('#booksTable tbody');
            booksTable.innerHTML = '';
            const books = Array.isArray(data) ? data : Object.values(data);
            books.forEach(book => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${book.codeBook}</td>
                    <td>${book.title}</td>
                    <td>${book.publicationDate}</td>
                    <td>${book.publisher}</td>
                    <td>${book.category}</td>
                    <td>${book.stockTotal}</td>
                    <td>
                        <button class="btn btn-primary" onclick="editBook(${book.codeBook})">Editar</button>
                        <button class="btn btn-danger" onclick="deleteBook(${book.codeBook})">Eliminar</button>
                    </td>
                `;
                booksTable.appendChild(row);
            });
        })
        .catch(error => console.error('Error loading books:', error));
}

function save(){
    //const modal= new bootstrap.Modal(document.getElementById('bookModal'));
    const method = curretentBookId ? 'PUT' : 'POST';
    const url = curretentBookId ? `http://localhost:8080/api/books/update/${curretentBookId}` : 'http://localhost:8080/api/books/add';
    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            title: document.getElementById('title').value,
            publicationDate: document.getElementById('publicationDate').value,
            publisher: document.getElementById('publisher').value,
            category: document.getElementById('category').value,
            stockTotal: document.getElementById('stockTotal').value
        })
    }).then(response => {
        if (response.ok) {
            loadBooks();
            resetear();
        }else{
            alert("Error al guardar el libro");
        }
    }).catch(error =>{
        console.error('Error saving book:', error);
    })
}

function editBook(codeBook){
    const modal= new bootstrap.Modal(document.getElementById('bookModal'));
    fetch(`http://localhost:8080/api/books/${codeBook}`)
        .then(response => response.json())
        .then(data => {
            console.log('Book data:', data);
            curretentBookId = data.codeBook;
            document.getElementById('title').value = data.title;
            document.getElementById('publicationDate').value = data.publicationDate;
            document.getElementById('publisher').value = data.publisher;
            document.getElementById('category').value = data.category;
            document.getElementById('stockTotal').value = data.stockTotal;
            modal.show();
        })
        .catch(error => console.error('Error loading book:', error));
}

function deleteBook(code){
    if (confirm('¿Estás seguro de que deseas eliminar este libro?')) {
        fetch(`http://localhost:8080/api/books/delete/${code}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                loadBooks();
            } else {
                alert("Error al eliminar el libro");
            }
        })
        .catch(error => console.error('Error deleting book:', error));
    }
}

function resetear(){
    const bookForm = document.getElementById('bookForm');
    bookForm.reset();
    curretentBookId = null;
}
