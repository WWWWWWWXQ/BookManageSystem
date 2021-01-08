package com.wxq.web.service.impl;

import com.wxq.web.dao.po.User;
import com.wxq.web.entity.BorrowRecord;
import com.wxq.web.exception.UserNotFoundException;
import com.wxq.web.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class DefaultBookService implements BookService {
    @Override
    public boolean returnBook(User userDTO, String bookId) {
        return false;
    }

    @Override
    public boolean borrowBook(User userDTO, String bookId) {
        return false;
    }

    @Override
    public List<Book> getBooksByBookName(String bookName) {
        return null;
    }

    @Override
    public boolean deleteBook(String bookId) {
        return false;
    }

    @Override
    public boolean addBook(Book book) {
        return false;
    }

    @Override
    public boolean addBooks(List<Book> list) {
        return false;
    }

    @Override
    public List<BorrowRecord> getBorrowRecordByName(String userName) {
        return null;
    }

    @Override
    public User getUserByName(String userName) throws UserNotFoundException {
        return null;
    }

    @Override
    public boolean addUser(User userDTO) {
        return false;
    }

    @Override
    public boolean payBookCost(User userDTO) {
        return false;
    }

    @Override
    public boolean isInitialized(List<Book> books, Book book) {
        return false;
    }

    @Override
    public LinkedList<Book> getAllBooks() {
        return null;
    }

    @Override
    public LinkedList<User> getAllUsers() {
        return null;
    }

    @Override
    public String getUserTypeByName(String userName) {

        return null;
    }

    @Override
    public String getBookNameByBookId(String bookId) {
        return null;
    }
}
