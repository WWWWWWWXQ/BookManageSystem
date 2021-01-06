package com.wxq.web.dao;

import com.wxq.web.dao.po.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
