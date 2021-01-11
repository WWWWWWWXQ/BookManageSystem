package com.wxq.web.service;

import java.util.LinkedList;
import java.util.List;

import com.wxq.web.controller.vo.BookVO;
import com.wxq.web.dao.po.User;
import com.wxq.web.exception.UserNotFoundException;
import com.wxq.web.service.dto.BookDTO;
import com.wxq.web.service.dto.BorrowRecordDTO;

public interface BookService {

	boolean returnBook(User userDTO, String bookId);

 	boolean borrowBook(User userDTO, String bookId);

	List<BookDTO> getBooksByBookName(String bookName);
	
	void deleteBook(Long bookId);

	boolean addBook(BookDTO book);

	boolean addBooks(List<BookDTO> list);

	List<BorrowRecordDTO> getBorrowRecordByName(String userName);

	User getUserByName(String userName)throws UserNotFoundException;

	boolean addUser(User userDTO);
	
	boolean payBookCost(User userDTO);

	LinkedList<BookDTO> getAllBooks();

	LinkedList<User> getAllUsers();

	String getUserTypeByName(String userName);

	String getBookNameByBookId(String bookId);

    List<BookVO> findAll();

	BookDTO findByBookId(Long bookId);
}
