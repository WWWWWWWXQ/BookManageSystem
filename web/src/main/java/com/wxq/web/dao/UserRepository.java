package com.wxq.web.dao;

import com.wxq.web.dao.po.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserDTO, Long> {
}
