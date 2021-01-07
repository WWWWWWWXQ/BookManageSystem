package com.wxq.common.base;


import lombok.Getter;

public enum Role {
    TEACHER("teacher"),
    STUDENT("student");

    @Getter
    private String role;

    private Role(String role){
        this.role = role;
    }

}
