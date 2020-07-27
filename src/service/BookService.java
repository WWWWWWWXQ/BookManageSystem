package service;

import java.util.LinkedList;
import java.util.List;

import entity.Book;
import entity.BorrowRecord;
import entity.User;
import exception.UserNotFoundException;

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
