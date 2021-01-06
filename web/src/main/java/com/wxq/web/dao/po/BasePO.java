package com.wxq.web.dao.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Date;

@Data
@MappedSuperclass
public class BasePO {

    @Id
    private Long id;

    private Date createTime;
    private Date updateTime;

}
