package com.wxq.web.service.impl;

import com.wxq.web.dao.BookRepository;
import com.wxq.web.dao.po.Book;
import com.wxq.web.dao.po.User;
import com.wxq.web.exception.UserNotFoundException;
import com.wxq.web.service.BookService;
import com.wxq.web.service.dto.BookDTO;
import com.wxq.web.service.dto.BorrowRecordDTO;
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
    public boolean returnBook(User userDTO, String bookId) {
        return false;
    }

    @Override
    public boolean borrowBook(User userDTO, String bookId) {
        return false;
    }

//    @Override
//    public List<BookDTO> getBooksByBookName(String bookName) {
//        return null;
//    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

//    @Override
//    public boolean addBook(BookDTO book) {
//        return false;
//    }
//
//    @Override
//    public boolean addBooks(List<BookDTO> list) {
//        return false;
//    }
//
//    @Override
//    public List<BorrowRecordDTO> getBorrowRecordByName(String userName) {
//        return null;
//    }
//
//    @Override
//    public User getUserByName(String userName) throws UserNotFoundException {
//        return null;
//    }

//    @Override
//    public boolean addUser(User userDTO) {
//        return false;
//    }
//
//    @Override
//    public boolean payBookCost(User userDTO) {
//        return false;
//    }
//
//    @Override
//    public LinkedList<BookDTO> getAllBooks() {
//        return null;
//    }
//
//    @Override
//    public LinkedList<User> getAllUsers() {
//        return null;
//    }
//
//    @Override
//    public String getUserTypeByName(String userName) {
//        return null;
//    }
//
//    @Override
//    public String getBookNameByBookId(String bookId) {
//        return null;
//    }

    @Override
    public List<BookDTO> findAll() {
        Iterable<Book> books = bookRepository.findAll();
        List<BookDTO> bookDTOS = new LinkedList<>();
        while (books.iterator().hasNext()){
            BookDTO bookDTO = new BookDTO();
            BeanUtils.copyProperties(books.iterator(), bookDTO);
            bookDTOS.add(bookDTO);
        }
        return bookDTOS;
    }

    @Override
    public BookDTO findByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId).get();
        if (book == null)
            throw new RuntimeException();
        BookDTO bookDTO = new BookDTO();
        BeanUtils.copyProperties(book, bookDTO);
        return bookDTO;
    }
}
