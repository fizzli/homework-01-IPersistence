package com.fizzli.sqlSession;

import com.fizzli.pojo.Configuration;
import com.fizzli.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {

    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    int executeUpdate(Configuration configuration, MappedStatement mappedStatement, Object[] params) throws SQLException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException;
}
