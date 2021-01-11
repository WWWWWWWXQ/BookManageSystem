package com.wxq.web.controller;

import com.wxq.web.controller.vo.BookVO;
import com.wxq.web.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping("books")
    public List<BookVO> books(){
        return bookService.findAll();
    }
}
