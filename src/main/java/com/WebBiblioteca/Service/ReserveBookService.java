package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.PageResponse;
import com.WebBiblioteca.DTO.ReserveBook.*;
import com.WebBiblioteca.DTO.Usuario.UserResponse;
import com.WebBiblioteca.Exception.ExcelGenerationException;
import com.WebBiblioteca.Exception.NoDataFoundException;
import com.WebBiblioteca.Exception.ResourceNotFoundException;
import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Model.ReserveBook;
import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.ReserveBookRepository;
import com.WebBiblioteca.Utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;


@Service
public class ReserveBookService {
    private final ReserveBookRepository reserveBookRepository;
    private final BookService bookService;
    private final UserService userService;
    public ReserveBookService(ReserveBookRepository reserveBookRepository,BookService bookService,UserService userService) {
        this.reserveBookRepository = reserveBookRepository;
        this.bookService = bookService;
        this.userService = userService;
    }
    public PageResponse<ReserveBookResponse> getReservationList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReserveBook> reserveBooks = reserveBookRepository.findAll(pageable);
        return mapToPageResponse(reserveBooks);
    }

    public List<ReserveBookResponse> getReservationActives() {
        return reserveBookRepository.findByState(true).stream().map(this::convertToReserveBookResponse)
                .toList();
    }
    public List<ReserveBookResponse> gerReservationInactive() {
        return reserveBookRepository.findByState(false).stream().map(this::convertToReserveBookResponse)
                .toList();
    }
    public ReserveBookResponse getReservationById(Long id){
        ReserveBook reserveBook = reserveBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ReserveBook", "id", id));
        return convertToReserveBookResponse(reserveBook);
    }
    public List<ReserveBookResponse> getReservationByUser(Long id) {
        return reserveBookRepository.findByUserId(id).stream().map(this::convertToReserveBookResponse)
                .toList();
    }
    public List<ReserveBookResponse> getReservationByDni(String dni) {
        return reserveBookRepository.findByUserDni(dni).stream().map(this::convertToReserveBookResponse)
                .toList();
    }
    public ReserveBookResponse saveReservation(ReserveBookRequest request){
        User user = userService.getUser(request.getUserId());
        User librarian = userService.getUser(request.getLibraryId());
        if (bookService.verifyBook(request.getBookId())) {
            Book book = bookService.updateBookStock(request.getBookId(), -1);
            ReserveBook reserveBook = new ReserveBook();
            reserveBook.setUser(user);
            reserveBook.setLibrarian(librarian);
            reserveBook.setBook(book);
            reserveBook.setState(true);
            reserveBook.setDateReserve(LocalDate.now());
            reserveBook.setStartTime(LocalTime.now());
            reserveBook.setEndTime(LocalTime.now().plusHours(4));
            ReserveBook reserveBookSave = reserveBookRepository.save(reserveBook);
            bookService.updateBookStock(request.getBookId(), -1);
            return convertToReserveBookResponse(reserveBookSave);
        } else {
            throw new IllegalArgumentException("Some books are not available for loan.");
        }
    }
    public ReserveBookResponse updateReservation(ReserveBookUpdate request,Long id){
        ReserveBook reserveBook = reserveBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reserve","id",id));
        if(request.getBookId() !=null){
            reserveBook.setBook(bookService.getBook(request.getBookId()));
        }
        if (request.getStartTime() !=null){
            reserveBook.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() !=null){
            reserveBook.setEndTime(request.getEndTime());
        }
        if(request.getReservationDate() != null){
            reserveBook.setDateReserve(request.getReservationDate());
        }
        if(request.getIsActive() != null){
            reserveBook.setState(request.getIsActive());
        }
        ReserveBook response = reserveBookRepository.save(reserveBook);
        return convertToReserveBookResponse(response);
    }

    public void deleteReservation(Long id){
        ReserveBook reserveBook = reserveBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation","id",id));
        reserveBook.setState(false);
        reserveBookRepository.save(reserveBook);
    }

    public List<ReserveBookResponse> getReservationsByBookTitle(String title){
        List<ReserveBook> reserveBooks = reserveBookRepository.findByBookTitleContains(title);
        if (reserveBooks.isEmpty()) {
            throw new ResourceNotFoundException("Reservation", "title", title);
        }
        return reserveBooks.stream().map(this::convertToReserveBookResponse)
                .toList();
    }

    public List<ReserveBookResponse> getReservationByDate(LocalDate date){
       return reserveBookRepository.findByDateReserve(date).stream().map(this::convertToReserveBookResponse)
                .toList();
    }

    public byte[] createReportExcel(ReserveBookReportRequest request){
        List<ReserveBookReportDto> reportDtoList = getReserveBookHistory(request.getDateStart(),request.getDateEnd());
        if(reportDtoList.isEmpty()){
            throw new NoDataFoundException("No se encontraron datos para los parámetros proporcionados");
        }
        try(Workbook wb = new XSSFWorkbook()){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            String safeName = WorkbookUtil.createSafeSheetName("Report of the reserves");
            Sheet sheet = wb.createSheet(safeName);
            Row row = sheet.createRow(0);
            ExcelUtils.createCell(wb,row,0,"Fecha de la reserva");
            ExcelUtils.createCell(wb,row,1,"Hora de inicio");
            ExcelUtils.createCell(wb,row,2,"Hora de fin");
            ExcelUtils.createCell(wb,row,3,"Libro");
            ExcelUtils.createCell(wb,row,4,"Usuario");
            ExcelUtils.createCell(wb,row,5,"Bibliotecario");

            int rowCount = 1;
            for(ReserveBookReportDto reportDto: reportDtoList){
                Row rowItem = sheet.createRow(rowCount++);
                ExcelUtils.createCell(wb,rowItem,0,reportDto.getDateReserve());
                ExcelUtils.createCell(wb,rowItem,1,reportDto.getTimeStart());
                ExcelUtils.createCell(wb,rowItem,2,reportDto.getTimeEnd());
                ExcelUtils.createCell(wb,rowItem,3,reportDto.getTitleBook());
                ExcelUtils.createCell(wb,rowItem,4,reportDto.getUserName());
                ExcelUtils.createCell(wb,rowItem,5,reportDto.getLibrarianName());
            }
            wb.write(outputStream);
            return outputStream.toByteArray();
        }catch (IOException e){
            throw new ExcelGenerationException("Report of the reserves");
        }

    }

    public List<ReserveBookReportDto> getReserveBookHistory(LocalDate dateStart,LocalDate dateEnd){
        List<Object[]> list = reserveBookRepository.getHistoryReservation(dateStart,dateEnd);
        List<ReserveBookReportDto> reserveBookReportDto = new LinkedList<>();
        for(Object[] row:list){
            ReserveBookReportDto reportDto = new ReserveBookReportDto(
                    ((java.sql.Date) row[0]).toLocalDate(),
                    ((java.sql.Time) row[1]).toLocalTime(),
                    ((java.sql.Time) row[2]).toLocalTime(),
                    (String) row[3],
                    (String) row[4],
                    (String) row[5]
            );
            reserveBookReportDto.add(reportDto);
        }
        return reserveBookReportDto;
    }
    public List<ReserveBookReportDto> getReserveBookHistory(String start,String end){
        LocalDate dateStart;
        LocalDate dateEnd;
        dateStart = (start == null || start.isEmpty()) ? null : LocalDate.parse(start);
        dateEnd = (end == null || end.isEmpty()) ? null : LocalDate.parse(end);
        List<Object[]> list = reserveBookRepository.getHistoryReservation(dateStart,dateEnd);
        List<ReserveBookReportDto> reserveBookReportDto = new LinkedList<>();
        for(Object[] row:list){
            ReserveBookReportDto reportDto = new ReserveBookReportDto(
                    ((java.sql.Date) row[0]).toLocalDate(),
                    ((java.sql.Time) row[1]).toLocalTime(),
                    ((java.sql.Time) row[2]).toLocalTime(),
                    (String) row[3],
                    (String) row[4],
                    (String) row[5]
            );
            reserveBookReportDto.add(reportDto);
        }
        return reserveBookReportDto;
    }

    public List<ReserveBookResponse> getReservationsByBookTitleOrAuthorNameOrAuthorLastName(String query, Long userId) {
        List<ReserveBook> reserveBooks = reserveBookRepository.findByBookTitleOrAuthorNameOrAuthorLastName(query, userId);
        if (reserveBooks.isEmpty()) {
            throw new ResourceNotFoundException("Reservation", "title or author name", query + " or " + query);
        }
        return reserveBooks.stream().map(this::convertToReserveBookResponse)
                .toList();
    }

    private ReserveBookResponse convertToReserveBookResponse(ReserveBook reserveBooks) {
        return new ReserveBookResponse(
                reserveBooks.getCodeReserve(),
                reserveBooks.getBook().getTitle(),
                reserveBooks.getUser().getCode(),
                reserveBooks.getUser().getName()+" "+reserveBooks.getUser().getLastname(),
                reserveBooks.getLibrarian().getCode(),
                reserveBooks.getLibrarian().getName() +" "+reserveBooks.getLibrarian().getLastname(),
                reserveBooks.getState(),
                reserveBooks.getDateReserve(),
                reserveBooks.getStartTime(),
                reserveBooks.getEndTime(),
                reserveBooks.getBook().getAutores().stream()
                        .map(author -> author.getNames() + " " + author.getLastname())
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("Unknown Author"),
                reserveBooks.getBook().getCategory()
        );
    }

    public List<ReserveBookResponse> getReservationByDateAndUserCode(LocalDate date, Long userCode) {
    return reserveBookRepository.findByDateReserveAndUserCode(date, userCode).stream()
            .map(this::convertToReserveBookResponse)
            .toList();
    }

    public List<ReserveBookResponse> getReservationsByStateAndUserCode(boolean state, Long userCode) {
        List<ReserveBook> reserveBooks = reserveBookRepository.findByStateAndUserCode(state, userCode);
        if (reserveBooks.isEmpty()) {
            throw new ResourceNotFoundException("Reservation", "state and user code", state + " and " + userCode);
        }
        return reserveBooks.stream().map(this::convertToReserveBookResponse).toList();
    }

    private PageResponse<ReserveBookResponse> mapToPageResponse(Page<ReserveBook> booksPage){
        List<ReserveBookResponse> content = booksPage.getContent()
                .stream()
                .map(this::convertToReserveBookResponse)
                .toList();

        return new PageResponse<>(
                content,
                booksPage.getNumber(),
                booksPage.getSize(),
                booksPage.getTotalElements(),
                booksPage.getTotalPages(),
                booksPage.isLast()
        );
    }
}
