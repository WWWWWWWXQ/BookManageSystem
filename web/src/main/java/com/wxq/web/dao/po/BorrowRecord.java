package com.wxq.web.dao.po;


import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class BorrowRecord extends BasePO{
    private Long bookId;
    private Long userId;
    private Date borrowTime;
    private Date returnTime;
    private double bill;
}
