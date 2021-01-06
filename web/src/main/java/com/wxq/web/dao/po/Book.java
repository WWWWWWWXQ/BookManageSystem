package com.wxq.web.dao.po;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Book extends BasePO {
    private int bookId;
    private String bookName;
    private String author;
    private String publishHouse;
    private boolean inStore;
    private String borrower;
}
