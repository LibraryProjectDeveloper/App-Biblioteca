
fetch('http://localhost:8080/api/books/')
    .then(response => response.json())
    .then(data => {

        const table = document.createElement('table');
        table.style.borderCollapse = 'collapse';
        table.style.width = '100%';


        const headerRow = document.createElement('tr');
        const headers = ['Código', 'Título', 'Fecha de Publicación', 'Editorial', 'Categoría', 'Stock Total'];

        headers.forEach(text => {
            const th = document.createElement('th');
            th.textContent = text;
            th.style.border = '1px solid #ddd';
            th.style.padding = '8px';
            th.style.backgroundColor = '#f2f2f2';
            headerRow.appendChild(th);
        });

        table.appendChild(headerRow);


        for (const key in data) {
            if (data.hasOwnProperty(key)) {
                const book = data[key];
                const row = document.createElement('tr');

                const cells = [
                    book.codeBook,
                    book.title,
                    book.publicationDate,
                    book.publisher,
                    book.category,
                    book.stockTotal
                ];

                cells.forEach(cellData => {
                    const td = document.createElement('td');
                    td.textContent = cellData;
                    td.style.border = '1px solid #ddd';
                    td.style.padding = '8px';
                    td.style.textAlign = 'center';
                    row.appendChild(td);
                });

                table.appendChild(row);
            }
        }


        document.body.appendChild(table);
    })
    .catch(error => console.error('Error al cargar los libros:', error));
