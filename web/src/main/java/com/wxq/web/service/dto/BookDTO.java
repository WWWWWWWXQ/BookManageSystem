package com.wxq.web.service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookDTO {
    private String bookName;
    private String author;
    private String publishHouse;
    private LocalDateTime publishTime;
    private Integer Edition;
}
