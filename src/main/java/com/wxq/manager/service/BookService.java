package com.wxq.manager.service;

import java.util.LinkedList;
import java.util.List;

import com.wxq.manager.entity.Book;
import com.wxq.manager.entity.BorrowRecord;
import com.wxq.manager.entity.User;
import com.wxq.manager.exception.UserNotFoundException;

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
