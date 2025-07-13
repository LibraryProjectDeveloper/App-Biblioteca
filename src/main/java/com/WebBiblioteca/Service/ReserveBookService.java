package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.ReserveBook.*;
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
    public List<ReserveBookResponse> getReservationList() {
        return reserveBookRepository.findAll().stream().map(reserveBook -> new ReserveBookResponse(
                reserveBook.getCodeReserve(),
                reserveBook.getBook().getTitle(),
                reserveBook.getUser().getCode(),
                reserveBook.getUser().getName()+" "+reserveBook.getUser().getLastname(),
                reserveBook.getLibrarian().getCode(),
                reserveBook.getLibrarian().getName() +" "+reserveBook.getLibrarian().getLastname(),
                reserveBook.getState(),
                reserveBook.getDateReserve(),
                reserveBook.getStartTime(),
                reserveBook.getEndTime()))
                .toList();
    }

    public List<ReserveBookResponse> getReservationActives() {
        return reserveBookRepository.findByState(true).stream().map(reserveBook -> new ReserveBookResponse(
                reserveBook.getCodeReserve(),
                reserveBook.getBook().getTitle(),
                reserveBook.getUser().getCode(),
                reserveBook.getUser().getName()+" "+reserveBook.getUser().getLastname(),
                reserveBook.getLibrarian().getCode(),
                reserveBook.getLibrarian().getName() +" "+reserveBook.getLibrarian().getLastname(),
                reserveBook.getState(),
                reserveBook.getDateReserve(),
                reserveBook.getStartTime(),
                reserveBook.getEndTime()))
                .toList();
    }
    public List<ReserveBookResponse> gerReservationInactive() {
        return reserveBookRepository.findByState(false).stream().map(reserveBook -> new ReserveBookResponse(
                reserveBook.getCodeReserve(),
                reserveBook.getBook().getTitle(),
                reserveBook.getUser().getCode(),
                reserveBook.getUser().getName(),
                reserveBook.getLibrarian().getCode(),
                reserveBook.getLibrarian().getName() +" "+reserveBook.getLibrarian().getLastname(),
                reserveBook.getState(),
                reserveBook.getDateReserve(),
                reserveBook.getStartTime(),
                reserveBook.getEndTime()))
                .toList();
    }
    public ReserveBookResponse getReservationById(Long id){
        ReserveBook reserveBook = reserveBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ReserveBook", "id", id));
        return new ReserveBookResponse(
                reserveBook.getCodeReserve(),
                reserveBook.getBook().getTitle(),
                reserveBook.getUser().getCode(),
                reserveBook.getUser().getName()+" "+reserveBook.getUser().getLastname(),
                reserveBook.getLibrarian().getCode(),
                reserveBook.getLibrarian().getName() +" "+reserveBook.getLibrarian().getLastname(),
                reserveBook.getState(),
                reserveBook.getDateReserve(),
                reserveBook.getStartTime(),
                reserveBook.getEndTime()
        );
    }
    public List<ReserveBookResponse> getReservationByUser(Long id) {
        return reserveBookRepository.findByUserId(id).stream().map(reserveBook -> new ReserveBookResponse(
                reserveBook.getCodeReserve(),
                reserveBook.getBook().getTitle(),
                reserveBook.getUser().getCode(),
                reserveBook.getUser().getName()+" "+reserveBook.getUser().getLastname(),
                reserveBook.getLibrarian().getCode(),
                reserveBook.getLibrarian().getName() +" "+reserveBook.getLibrarian().getLastname(),
                reserveBook.getState(),
                reserveBook.getDateReserve(),
                reserveBook.getStartTime(),
                reserveBook.getEndTime()))
                .toList();
    }
    public List<ReserveBookResponse> getReservationByDni(String dni) {
        return reserveBookRepository.findByUserDni(dni).stream().map(reserveBook -> new ReserveBookResponse(
                        reserveBook.getCodeReserve(),
                        reserveBook.getBook().getTitle(),
                        reserveBook.getUser().getCode(),
                        reserveBook.getUser().getName()+" "+reserveBook.getUser().getLastname(),
                        reserveBook.getLibrarian().getCode(),
                        reserveBook.getLibrarian().getName() +" "+reserveBook.getLibrarian().getLastname(),
                        reserveBook.getState(),
                        reserveBook.getDateReserve(),
                        reserveBook.getStartTime(),
                        reserveBook.getEndTime()))
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
            return new ReserveBookResponse(
                    reserveBookSave.getCodeReserve(),
                    reserveBookSave.getBook().getTitle(),
                    reserveBookSave.getUser().getCode(),
                    reserveBookSave.getUser().getName()+" "+reserveBookSave.getUser().getLastname(),
                    reserveBookSave.getLibrarian().getCode(),
                    reserveBookSave.getLibrarian().getName() +" "+reserveBookSave.getLibrarian().getLastname(),
                    reserveBookSave.getState(),
                    reserveBookSave.getDateReserve(),
                    reserveBookSave.getStartTime(),
                    reserveBookSave.getEndTime()
            );
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
        return new ReserveBookResponse(
                response.getCodeReserve(),
                response.getBook().getTitle(),
                response.getUser().getCode(),
                response.getUser().getName()+" "+response.getUser().getLastname(),
                response.getLibrarian().getCode(),
                response.getLibrarian().getName() +" "+response.getLibrarian().getLastname(),
                response.getState(),
                response.getDateReserve(),
                response.getStartTime(),
                response.getEndTime()
        );
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
        return reserveBooks.stream().map(reserveBook -> new ReserveBookResponse(
                reserveBook.getCodeReserve(),
                reserveBook.getBook().getTitle(),
                reserveBook.getUser().getCode(),
                reserveBook.getUser().getName()+" "+reserveBook.getUser().getLastname(),
                reserveBook.getLibrarian().getCode(),
                reserveBook.getLibrarian().getName() +" "+reserveBook.getLibrarian().getLastname(),
                reserveBook.getState(),
                reserveBook.getDateReserve(),
                reserveBook.getStartTime(),
                reserveBook.getEndTime()))
                .toList();
    }

    public List<ReserveBookResponse> getReservationByDate(LocalDate date){
       return reserveBookRepository.findByDateReserve(date).stream().map(reserveBook -> new ReserveBookResponse(
                reserveBook.getCodeReserve(),
                reserveBook.getBook().getTitle(),
                reserveBook.getUser().getCode(),
                reserveBook.getUser().getName()+" "+reserveBook.getUser().getLastname(),
                reserveBook.getLibrarian().getCode(),
                reserveBook.getLibrarian().getName() +" "+reserveBook.getLibrarian().getLastname(),
                reserveBook.getState(),
                reserveBook.getDateReserve(),
                reserveBook.getStartTime(),
                reserveBook.getEndTime()))
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
}
