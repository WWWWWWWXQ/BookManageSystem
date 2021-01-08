package com.wxq.web.service.dto;

import com.wxq.common.base.Gender;
import com.wxq.common.base.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String userName;
    //用户类型 学生为stu，教师为tea
    private Role role;
    //当前借阅数量（未还）
    private int borrowNum;
    //欠费总额
    private double costAmount;

    private Gender gender;

    public UserDTO(String userName, Role role, Gender gender){
        this.id = null;
        this.userName=userName;
        this.role=role;
        this.borrowNum = 0;
        this.costAmount = 0;
        this.gender = gender;
    }
}
