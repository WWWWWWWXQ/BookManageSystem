package com.wxq.common.base;

import lombok.Getter;

public enum Gender {

    MALE("male"),
    FEMALE("female");

    @Getter
    private String gender;

    private Gender(String gender){
        this.gender = gender;
    }

}
