package com.wxq.web.controller;

import com.wxq.web.controller.vo.BookVO;
import com.wxq.web.exception.BookNotFoundException;
import com.wxq.web.service.BookService;
import com.wxq.web.service.dto.BookDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping(value = "/books")
    public List<BookVO> books(){
        return bookService.findAll();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void delete(@RequestParam("id") Long bookId){
        try {
            BookDTO bookDTO = bookService.findByBookId(bookId);
        }catch (Exception e){
            log.info("book: [{}] no found", bookId);
        }
    }
}
