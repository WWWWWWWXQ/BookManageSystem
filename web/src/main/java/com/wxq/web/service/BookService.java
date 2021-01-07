package com.wxq.web.service;

import java.util.LinkedList;
import java.util.List;

import com.wxq.web.entity.Book;
import com.wxq.web.entity.BorrowRecord;
import com.wxq.web.dao.po.User;
import com.wxq.web.exception.UserNotFoundException;

public interface BookService {

	boolean returnBook(User user, String bookId);

 	boolean borrowBook(User user, String bookId);

	List<Book> getBooksByBookName(String bookName);
	
	boolean deleteBook(String bookId);

	boolean addBook(Book book);

	boolean addBooks(List<Book> list);

	List<BorrowRecord> getBorrowRecordByName(String userName);

	User getUserByName(String userName)throws UserNotFoundException;

	boolean addUser(User user);
	
	boolean payBookCost(User user);

	boolean isInitialized(List<Book> books, Book book);

	LinkedList<Book> getAllBooks();

	LinkedList<User> getAllUsers();

	String getUserTypeByName(String userName);

	String getBookNameByBookId(String bookId);
}
