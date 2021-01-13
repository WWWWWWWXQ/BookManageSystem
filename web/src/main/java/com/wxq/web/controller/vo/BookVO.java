package com.wxq.web.controller.vo;

import com.wxq.web.service.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookVO {
    private String bookName;
    private String author;
    private String publishHouse;
    private Date publishTime;
    private Integer Edition;
    private UserDTO borrower;
    private Date borrowTime;
}
