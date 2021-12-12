package com.tz;

import java.util.List;

public interface UserDao {
    User findById(Long id);
    User findByName(String name);
    List<User> findAll();
    void save(User user);
    void updateNameById(Long id, String newName);
}
