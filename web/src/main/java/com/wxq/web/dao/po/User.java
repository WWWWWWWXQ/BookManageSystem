package com.wxq.web.dao.po;

import com.wxq.common.base.Role;
import com.wxq.common.base.Sex;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class User extends BasePO{

    private String userName;
    //用户类型 学生为stu，教师为tea
    private Role role;
    //当前借阅数量（未还）
    private int bookNum;
    //欠费总额
    private double costAmount;

    private Sex sex;
}
