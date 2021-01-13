package com.wxq.web.service;

import java.util.List;

import com.wxq.web.dao.po.UserDTO;
import com.wxq.web.service.dto.BookDTO;

public interface BookService {

	boolean returnBook(UserDTO userDTO, Long bookId);

 	boolean borrowBook(UserDTO userDTO, Long bookId);
	
	void deleteBook(Long bookId);

	void addBooks(List<BookDTO> list);



    List<BookDTO> findAll();

	BookDTO findByBookId(Long bookId);
}
