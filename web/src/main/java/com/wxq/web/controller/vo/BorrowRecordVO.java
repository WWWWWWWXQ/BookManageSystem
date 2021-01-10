package com.wxq.web.controller.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BorrowRecordVO {
    private Long recordId;
    private Long bookId;
    private Long userId;
    private Date borrowTime;
    private Date returnTime;
    private Long bill;
}
