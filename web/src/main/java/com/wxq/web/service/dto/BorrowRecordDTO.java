package com.wxq.web.service.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BorrowRecordDTO {
    private Long recordId;
    private Long bookId;
    private Long userId;
    private Date borrowTime;
    private Date returnTime;
    private Long bill;
}
