package com.wxq.web.controller.vo;

import com.wxq.web.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class BookVO {
    private String bookName;
    private String author;
    private String publishHouse;
    private LocalDateTime publishTime;
    private Integer Edition;
    private User borrower;
    private LocalDateTime borrowTime;
}
