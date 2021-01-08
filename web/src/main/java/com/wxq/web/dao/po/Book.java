package com.wxq.web.dao.po;

import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class Book extends BasePO {
    private String bookName;
    private String author;
    private String publishHouse;
    private boolean inStore;
    private String borrower;
    private Date storeTime;
}
