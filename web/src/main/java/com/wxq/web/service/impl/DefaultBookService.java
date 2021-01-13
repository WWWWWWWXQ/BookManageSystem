package com.wxq.web.service.impl;

import com.wxq.web.dao.BookRepository;
import com.wxq.web.dao.po.Book;
import com.wxq.web.dao.po.UserDTO;
import com.wxq.web.service.BookService;
import com.wxq.web.service.dto.BookDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class DefaultBookService implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Override
    public boolean returnBook(UserDTO userDTO, Long bookId) {
        Book book = bookRepository.findById(bookId).get();
        if (! book.getBorrower().equals(userDTO.getUserName())){
            log.info("The borrower of book 《{}》 is not {}", book.getBookName(), userDTO.getUserName());
            return false;
        }
        book.setBorrower("null");
        book.setInStore(true);
        return true;
    }

    @Override
    public boolean borrowBook(UserDTO userDTO, Long bookId) {
        Book book = bookRepository.findById(bookId).get();
        if (!book.isInStore()){
            log.info("Book [{}] has been borrowed", bookId);
            return false;
        }
        book.setBorrower(userDTO.getUserName());
        book.setInStore(false);
        bookRepository.save(book);
        return true;
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public void addBooks(List<BookDTO> list) {
        List<Book> books = new LinkedList<>();
        for (BookDTO bookDTO : list) {
            Book book = new Book();
            BeanUtils.copyProperties(bookDTO, book);
            book.setId(null);
            books.add(book);
        }
        bookRepository.saveAll(books);
    }

    @Override
    public List<BookDTO> findAll() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> bookDTOS = new LinkedList<>();
        for (Book book : books) {
            BookDTO bookDTO = new BookDTO();
            BeanUtils.copyProperties(book, bookDTO);
            bookDTOS.add(bookDTO);
        }
        return bookDTOS;
    }

    @Override
    public BookDTO findByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId).get();
        if (book == null) throw new RuntimeException();
        BookDTO bookDTO = new BookDTO();
        BeanUtils.copyProperties(book, bookDTO);
        return bookDTO;
    }
}
