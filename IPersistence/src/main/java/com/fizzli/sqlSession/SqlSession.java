package com.fizzli.sqlSession;

import java.util.List;

public interface SqlSession {

    //查询所有
    public<E> List<E> selectList(String statementId, Object... params) throws Exception;

    //根据条件查询单个
    public<T> T selectOne(String statementId, Object... params) throws Exception;

    //增删改方法
    public int executeUpdate(String statementId, Object... params) throws Exception;

    public <T> T getMapper(Class<T> tClass);
}
