package com.wxq.web.service.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BookDTO {
    private Long Id;
    private String bookName;
    private String author;
    private String publishHouse;
    private boolean inStore;
    private String borrower;
    private Date storeTime;
}
