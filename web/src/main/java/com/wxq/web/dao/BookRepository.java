package com.wxq.web.dao;

import com.wxq.web.dao.po.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

}
