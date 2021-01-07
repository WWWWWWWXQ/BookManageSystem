package com.wxq.web.service.dto;

import com.wxq.common.base.Gender;
import com.wxq.common.base.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String userName;
    //用户类型 学生为stu，教师为tea
    private Role role;
    //当前借阅数量（未还）
    private int bookNum;
    //欠费总额
    private double costAmount;

    private Gender gender;
}
