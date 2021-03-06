package com.wxq.web.dao.po;

import com.wxq.common.base.Role;
import com.wxq.common.base.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;

@Data
@Entity
public class UserDTO extends BasePO{

    private java.lang.String userName;
    //用户类型 学生为stu，教师为tea
    private java.lang.String role;
    //当前借阅数量（未还）
    private int borrowNum;
    //欠费总额
    private double costAmount;

    private java.lang.String gender;

}
