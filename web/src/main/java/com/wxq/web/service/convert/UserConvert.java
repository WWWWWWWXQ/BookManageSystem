package com.wxq.web.service.convert;

import com.wxq.common.base.Gender;
import com.wxq.common.base.Role;
import com.wxq.web.dao.po.User;
import org.springframework.beans.BeanUtils;

public class UserConvert {
    public static com.wxq.web.service.dto.UserDTO toDTO(User po){
        com.wxq.web.service.dto.UserDTO dto = new com.wxq.web.service.dto.UserDTO();
        BeanUtils.copyProperties(po, dto);
        dto.setGender(Gender.valueOf(po.getGender()));
        dto.setRole(Role.valueOf(po.getRole()));
        return dto;
    }

    public static User toPo(com.wxq.web.service.dto.UserDTO dto){
        User po = new User();
        BeanUtils.copyProperties(dto, po);
        po.setGender(dto.getGender().name());
        po.setRole(dto.getRole().name());
        return po;
    }
}
