package com.wxq.web.service.convert;

import com.wxq.common.base.Gender;
import com.wxq.common.base.Role;
import com.wxq.web.dao.po.User;
import com.wxq.web.service.dto.UserDTO;
import lombok.val;
import org.springframework.beans.BeanUtils;

public class UserConvert {
    public static UserDTO toDTO(User po){
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(po, dto);
        dto.setGender(Gender.valueOf(po.getGender()));
        dto.setRole(Role.valueOf(po.getRole()));
        return dto;
    }

    public static User toPo(UserDTO dto){
        User po = new User();
        BeanUtils.copyProperties(dto, po);
        po.setGender(dto.getGender().name());
        po.setRole(dto.getRole().name());
        return po;
    }
}
