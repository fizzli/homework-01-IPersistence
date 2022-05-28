package com.fizzli.dao;

import com.fizzli.pojo.User;

import java.util.List;

public interface IUserDao {
    /**
     * 查询所有数据
     * @return
     */
    List<User> findAll();

    /**
     * 根据条件查询数据
     * @param user
     * @return
     */
    User findUserByCondition(User user);

    /**
     * 增加用户
     * @param user
     * @return
     */
    int addUser(User user);

    /**
     * 通过ID删除用户
     * @param user
     * @return
     */
    int delUserById(User user);

    /**
     * 通过ID修改用户信息
     * @param user
     * @return
     */
    int updateUserById(User user);
}
