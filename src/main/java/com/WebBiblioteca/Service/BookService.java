package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.Autor.AuthorRequest;
import com.WebBiblioteca.DTO.Autor.AuthorResponse;
import com.WebBiblioteca.DTO.Book.BookReportDto;
import com.WebBiblioteca.DTO.Book.BookRequest;
import com.WebBiblioteca.DTO.Book.BookResponse;
import com.WebBiblioteca.DTO.Book.CountBookByCategory;
import com.WebBiblioteca.Exception.DuplicateResourceException;
import com.WebBiblioteca.Exception.ExcelGenerationException;
import com.WebBiblioteca.Exception.NoDataFoundException;
import com.WebBiblioteca.Exception.ResourceNotFoundException;
import com.WebBiblioteca.Model.Author;
import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Model.BookState;
import com.WebBiblioteca.Model.Category;
import com.WebBiblioteca.Repository.BookReposity;
import com.WebBiblioteca.Utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookReposity bookReposity;
    private final AuthorService authorService;

    public BookService(BookReposity bookReposity, AuthorService authorService) {
        this.authorService = authorService;
        this.bookReposity = bookReposity;
    }

    public List<BookResponse> getBookList() {
        return bookReposity.findAll()
                .stream()
                .map(book -> new BookResponse(
                        book.getCodeBook(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublicationDate(),
                        book.getPublisher(),
                        book.getCategory(),
                        book.getStockTotal(),
                        book.getEstado(),
                        book.getAutores().stream()
                                .map(author -> new AuthorResponse(
                                        author.getIdAuthor(),
                                        author.getNames(),
                                        author.getLastname(),
                                        author.getNationality(),
                                        author.getBirthdate(),
                                        author.getGender()
                                )).collect(Collectors.toList())
                )).collect(Collectors.toList());
    }

    public List<BookResponse> getBookList(BookState bookState) {
        return bookReposity.findByEstado(bookState)
                .stream().
                map(book -> new BookResponse(
                        book.getCodeBook(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublicationDate(),
                        book.getPublisher(),
                        book.getCategory(),
                        book.getStockTotal(),
                        book.getEstado(),
                        book.getAutores().stream()
                                .map(author -> new AuthorResponse(
                                        author.getIdAuthor(),
                                        author.getNames(),
                                        author.getLastname(),
                                        author.getNationality(),
                                        author.getBirthdate(),
                                        author.getGender()
                                )).collect(Collectors.toList())
                )).collect(Collectors.toList());
    }

    public BookRequest getBookById(Long id) {
        Book book = bookReposity.findByCodeBook(id).orElseThrow(() -> new ResourceNotFoundException("Book not found", "id", id));
        return new BookRequest(
                book.getTitle(),
                book.getIsbn(),
                book.getPublicationDate(),
                book.getPublisher(),
                book.getCategory(),
                book.getStockTotal(),
                book.getEstado(),
                book.getAutores().stream()
                        .map(author -> new AuthorRequest(
                                author.getIdAuthor(),
                                author.getNames(),
                                author.getLastname(),
                                author.getNationality(),
                                author.getBirthdate(),
                                author.getGender()
                        )).collect(Collectors.toList())
        );
    }

    public List<Category> getAllCategories() {
        return bookReposity.findAll()
                .stream()
                .map(Book::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<BookResponse> getBooksByPublicationYear(Integer year) {
        return bookReposity.findByPublicationDateYear(year)
                .stream()
                .map(book -> new BookResponse(
                        book.getCodeBook(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublicationDate(),
                        book.getPublisher(),
                        book.getCategory(),
                        book.getStockTotal(),
                        book.getEstado(),
                        book.getAutores().stream()
                                .map(author -> new AuthorResponse(
                                        author.getIdAuthor(),
                                        author.getNames(),
                                        author.getLastname(),
                                        author.getNationality(),
                                        author.getBirthdate(),
                                        author.getGender()
                                )).collect(Collectors.toList())
                )).collect(Collectors.toList());
    }

    @Transactional
    public BookResponse addBook(BookRequest book) {
        if (bookReposity.findByIsbn(book.getIsbn()).isPresent()) {
            throw new DuplicateResourceException("Book with this ISBN already exists", "ISBN", book.getIsbn());
        }
        Book newBook = new Book();
        newBook.setTitle(book.getTitle());
        newBook.setPublisher(book.getPublisher());
        newBook.setPublicationDate(book.getPublicationDate());
        newBook.setCategory(book.getCategory());
        newBook.setStockTotal(book.getStockTotal());
        newBook.setIsbn(book.getIsbn());
        newBook.setEstado(BookState.ACTIVO);
        if (authorService.existsAuthors(new HashSet<>(book.getAuthor()))) {
            Set<Author> authors = book.getAuthor().stream()
                    .map(authorRequest -> authorService.getAuthorById(authorRequest.getIdAuthor()))
                    .collect(Collectors.toSet());
            newBook.setAutores(authors);
        }
        Book savedBook = bookReposity.save(newBook);
        return new BookResponse(
                savedBook.getCodeBook(),
                savedBook.getTitle(),
                savedBook.getIsbn(),
                savedBook.getPublicationDate(),
                savedBook.getPublisher(),
                savedBook.getCategory(),
                savedBook.getStockTotal(),
                savedBook.getEstado(),
                savedBook.getAutores().stream()
                        .map(author -> new AuthorResponse(
                                author.getIdAuthor(),
                                author.getNames(),
                                author.getLastname(),
                                author.getNationality(),
                                author.getBirthdate(),
                                author.getGender()
                        )).collect(Collectors.toList())
        );
    }

    public BookResponse updateBook(Long id, BookRequest book) {
        Book bookToUpdate = bookReposity.findByCodeBook(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        if (book.getTitle() != null) {
            bookToUpdate.setTitle(book.getTitle());
        }
        if (book.getPublisher() != null) {
            bookToUpdate.setPublisher(book.getPublisher());
        }
        if (book.getPublicationDate() != null) {
            bookToUpdate.setPublicationDate(book.getPublicationDate());
        }
        if (book.getCategory() != null) {
            bookToUpdate.setCategory(book.getCategory());
        }
        if (book.getStockTotal() != null) {
            bookToUpdate.setStockTotal(book.getStockTotal());
        }
        if (book.getIsbn() != null) {
            bookToUpdate.setIsbn(book.getIsbn());
        }
        if (book.getState() != null) {
            bookToUpdate.setEstado(book.getState());
        }
        if (book.getAuthor() != null && authorService.existsAuthors(new HashSet<>(book.getAuthor()))) {
            Set<Author> authors = book.getAuthor().stream()
                    .map(authorRequest -> authorService.getAuthorById(authorRequest.getIdAuthor()))
                    .collect(Collectors.toSet());
            bookToUpdate.setAutores(authors);
        }
        Book bookUpdate = bookReposity.save(bookToUpdate);
        return new BookResponse(
                bookUpdate.getCodeBook(),
                bookUpdate.getTitle(),
                bookUpdate.getIsbn(),
                bookUpdate.getPublicationDate(),
                bookUpdate.getPublisher(),
                bookUpdate.getCategory(),
                bookUpdate.getStockTotal(),
                bookUpdate.getEstado(),
                bookUpdate.getAutores().stream()
                        .map(author -> new AuthorResponse(
                                author.getIdAuthor(),
                                author.getNames(),
                                author.getLastname(),
                                author.getNationality(),
                                author.getBirthdate(),
                                author.getGender()
                        )).collect(Collectors.toList())
        );
    }

    public void deleteBook(Long id) {
        Book book = bookReposity.findByCodeBook(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setEstado(BookState.INACTIVO);
        bookReposity.save(book);
    }


    public List<BookResponse> filterBookCategory(Category category) {
        return bookReposity.findByCategory(category)
                .stream()
                .map(book -> new BookResponse(
                        book.getCodeBook(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublicationDate(),
                        book.getPublisher(),
                        book.getCategory(),
                        book.getStockTotal(),
                        book.getEstado(),
                        book.getAutores().stream()
                                .map(author -> new AuthorResponse(
                                        author.getIdAuthor(),
                                        author.getNames(),
                                        author.getLastname(),
                                        author.getNationality(),
                                        author.getBirthdate(),
                                        author.getGender()
                                )).collect(Collectors.toList())
                )).collect(Collectors.toList());
    }

    public boolean verifyBooks(List<Long> booklist) {
        for (Long book : booklist) {
            if (bookReposity.findById(book).isEmpty()) {
                return false;
            }
            if (isAvailable(book)) {
                return false;
            }
            if (getBookById(book).getStockTotal() <= 0) {
                return false;
            }
        }
        return true;
    }

    public boolean verifyBook(Long bookId) {
        if (bookReposity.findById(bookId).isEmpty()) {
            return false;
        }
        if (isAvailable(bookId)) {
            return false;
        }
        return getBookById(bookId).getStockTotal() > 0;
    }

    public boolean isAvailable(Long id) {
        Book book = bookReposity.findByCodeBook(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        return book.getEstado() != BookState.ACTIVO;
    }

    public Book getBook(Long id) {
        return bookReposity.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }

    public Set<Book> getBooksByIds(List<Long> bookIds) {
        return new HashSet<>(bookReposity.findAllById(bookIds));
    }

    public Set<Book> updateBooksStock(Set<Book> books) {
        Set<Book> updatedBooks = new HashSet<>();
        for (Book book : books) {
            Book existingBook = bookReposity.findById(book.getCodeBook())
                    .orElseThrow(() -> new ResourceNotFoundException("Book", "id", book.getCodeBook()));
            existingBook.setStockTotal(existingBook.getStockTotal() - 1);
            updatedBooks.add(bookReposity.save(existingBook));
        }
        return updatedBooks;
    }

    public Book updateBookStock(Long id, Integer stock) {
        Book book = bookReposity.findByCodeBook(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setStockTotal(book.getStockTotal() + stock);
        return bookReposity.save(book);
    }

    public List<BookResponse> getBooksByTitle(String title) {
        List<Book> bookList = Collections.emptyList();
        boolean titleValid = title != null && !title.isBlank();

        if (titleValid) {
            bookList = bookReposity.findByTitleContainingIgnoreCase(title);
        }

        return bookList.stream()
                .map(book -> new BookResponse(
                        book.getCodeBook(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublicationDate(),
                        book.getPublisher(),
                        book.getCategory(),
                        book.getStockTotal(),
                        book.getEstado())).collect(Collectors.toList());
    }

    public List<BookResponse> getBooksByCategoryName(String category) {
        List<Book> bookList = Collections.emptyList();
        boolean categoryValid = category != null && !category.isBlank();

        if (categoryValid) {
            bookList = bookReposity.findByCategory(Category.valueOf(category.toUpperCase()));
        }

        return bookList.stream()
                .map(book -> new BookResponse(
                        book.getCodeBook(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublicationDate(),
                        book.getPublisher(),
                        book.getCategory(),
                        book.getStockTotal(),
                        book.getEstado())).collect(Collectors.toList());
    }

    public byte[] createReportExcel(LocalDate dateStart, LocalDate dateEnd, String category) {
        if (dateStart == null || dateEnd == null || category == null) {
            throw new IllegalArgumentException();
        }
        List<BookReportDto> bookList = getPopularBooks(dateStart, dateEnd, category);
        if (!bookList.isEmpty()) {
            try (Workbook wb = new XSSFWorkbook()) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                String safeName = WorkbookUtil.createSafeSheetName("Most popular books report");
                Sheet sheet = wb.createSheet(safeName);
                Row row = sheet.createRow(0);
                ExcelUtils.createCell(wb, row, 0, "ID libro");
                ExcelUtils.createCell(wb, row, 1, "ISBN");
                ExcelUtils.createCell(wb, row, 2, "Libro");
                ExcelUtils.createCell(wb, row, 3, "Categoria");
                ExcelUtils.createCell(wb, row, 4, "Editorial");
                ExcelUtils.createCell(wb, row, 5, "Fecha de publicacion");
                ExcelUtils.createCell(wb, row, 6, "Cantidad de veces solicitado");

                int rowCount = 1;
                for (BookReportDto book : bookList) {
                    Row rowItem = sheet.createRow(rowCount++);
                    ExcelUtils.createCell(wb, rowItem, 0, book.getCodeBook());
                    ExcelUtils.createCell(wb, rowItem, 1, book.getIsbn());
                    ExcelUtils.createCell(wb, rowItem, 2, book.getTitle());
                    ExcelUtils.createCell(wb, rowItem, 3, book.getCategory());
                    ExcelUtils.createCell(wb, rowItem, 4, book.getPublisher());
                    ExcelUtils.createCell(wb, rowItem, 5, book.getPublicationDate());
                    ExcelUtils.createCell(wb, rowItem, 6, book.getQuantity());
                }
                wb.write(outputStream);
                return outputStream.toByteArray();
            } catch (IOException e) {
                throw new ExcelGenerationException("Most popular books report");
            }

        }
        throw new NoDataFoundException("No se encontraron datos para los parámetros proporcionados");
    }

    private List<BookReportDto> getPopularBooks(LocalDate dateStart, LocalDate dateEnd, String category) {
        List<BookReportDto> booksReportDto = new LinkedList<>();
        List<Object[]> booksList = bookReposity.getPopularBooks(dateStart, dateEnd, category);
        for (Object[] row : booksList) {
            BookReportDto bookReportDto = new BookReportDto(
                    (Long) row[0],
                    (String) row[1],
                    (String) row[2],
                    Category.valueOf((String) row[3]),
                    ((java.sql.Date) row[4]).toLocalDate(),
                    (String) row[5],
                    ((Long) row[6]).intValue()
            );
            booksReportDto.add(bookReportDto);
        }
        return booksReportDto;
    }

    public List<BookReportDto> getPopularBooks(String start, String end, String category) {
        LocalDate dateStart;
        LocalDate dateEnd;
        Category category1;
        dateStart = (start == null || start.isEmpty()) ? LocalDate.now().withDayOfMonth(1) : LocalDate.parse(start);
        dateEnd = (end == null || end.isEmpty()) ? LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()) : LocalDate.parse(end);
        category1 = (category == null || category.isEmpty()) ? null : Category.valueOf(category);
        List<BookReportDto> booksReportDto = new LinkedList<>();
        List<Object[]> booksList = bookReposity.getPopularBooks(dateStart, dateEnd, category);
        for (Object[] row : booksList) {
            BookReportDto bookReportDto = new BookReportDto(
                    (Long) row[0],
                    (String) row[1],
                    (String) row[2],
                    Category.valueOf((String) row[3]),
                    ((java.sql.Date) row[4]).toLocalDate(),
                    (String) row[5],
                    ((Long) row[6]).intValue()
            );
            booksReportDto.add(bookReportDto);
        }
        return booksReportDto;
    }

    public List<BookResponse> booksActiveAndStock() {
        return bookReposity.findAvailableBooksByState(BookState.ACTIVO)
                .stream()
                .map(book -> new BookResponse(
                        book.getCodeBook(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublicationDate(),
                        book.getPublisher(),
                        book.getCategory(),
                        book.getStockTotal(),
                        book.getEstado(),
                        book.getAutores().stream()
                                .map(author -> new AuthorResponse(
                                        author.getIdAuthor(),
                                        author.getNames(),
                                        author.getLastname(),
                                        author.getNationality(),
                                        author.getBirthdate(),
                                        author.getGender()
                                )).collect(Collectors.toList())
                )).collect(Collectors.toList());
    }

    public List<BookResponse> getBooksByAuthor(String authorName) {
        return bookReposity.searchAutor(authorName)
                .stream()
                .map(book -> new BookResponse(
                        book.getCodeBook(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublicationDate(),
                        book.getPublisher(),
                        book.getCategory(),
                        book.getStockTotal(),
                        book.getEstado(),
                        book.getAutores().stream()
                                .map(author -> new AuthorResponse(
                                        author.getIdAuthor(),
                                        author.getNames(),
                                        author.getLastname(),
                                        author.getNationality(),
                                        author.getBirthdate(),
                                        author.getGender()
                                )).collect(Collectors.toList())
                )).collect(Collectors.toList());
    }

    public List<CountBookByCategory> countBooksLoanedByCategory() {
        List<Object[]> rawResults = bookReposity.countBooksLoanedByCategory();
        return rawResults
                .stream()
                .map(row -> new CountBookByCategory(
                        (Category) row[0],
                        (Long) row[1]
                ))
                .collect(Collectors.toList());
    }
}
