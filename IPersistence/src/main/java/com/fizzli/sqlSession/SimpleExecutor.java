package com.fizzli.sqlSession;

import com.fizzli.config.Bound;
import com.fizzli.pojo.Configuration;
import com.fizzli.pojo.MappedStatement;
import com.fizzli.util.GenericTokenParser;
import com.fizzli.util.ParameterMapping;
import com.fizzli.util.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor{
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        PreparedStatement preparedStatement = getPreparedStatement(configuration, mappedStatement, params);
        //5.执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);

        ArrayList<Object> objects = new ArrayList<>();
        //6.返回结果
        while (resultSet.next()){
            Object o = resultTypeClass.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i < metaData.getColumnCount(); i++) {
                //数据库列名
                String columnName = metaData.getColumnName(i);
                //获取对应列的名字
                Object object = resultSet.getObject(columnName);
                //使用反射 内省 根据数据库表和实体的对应关系，完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);

                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,object);
            }
            objects.add(o);
        }

        return (List<E>) objects;
    }


    /**
     * 执行jdbc的增删改逻辑
     * @param configuration
     * @param mappedStatement
     * @param params
     * @return
     * @throws SQLException
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     */
    @Override
    public int executeUpdate(Configuration configuration, MappedStatement mappedStatement, Object[] params) throws SQLException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        PreparedStatement preparedStatement = getPreparedStatement(configuration, mappedStatement, params);
        int i = preparedStatement.executeUpdate();
        return i;
    }


    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        if (parameterType != null){
            Class<?> aClass = Class.forName(parameterType);
            return aClass;
        }
        return  null;

    }

    /**
     * 完成对#{}的解析工作：1.将#{}使用?代替 2.解析#{}里面的值进行存储
     * @param sql
     * @return
     */
    private Bound getBoundSql(String sql) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        //解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        return new Bound(parseSql,parameterMappings);
    }

    /**
     * 创建PreparedStatement 对象并返回。
     * @param configuration
     * @param mappedStatement
     * @param params
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private PreparedStatement getPreparedStatement(Configuration configuration, MappedStatement mappedStatement, Object[] params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        //1.注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();
        //2.获取 sql  select * from user where id=#{id} and username=#{username}
        //转换为 select * from user where id=? and username=?,还需要对#{}中的值存储
        String sql = mappedStatement.getSql();
        Bound boundSql = getBoundSql(sql);

        //3.获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        //4.设置参数
        //1).获取入参的类型字符串--全类名
        String parameterType = mappedStatement.getParameterType();
        //2).通过全类名转换成类
        Class<?> parameterTypeClass = getClassType(parameterType);

        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            // 3).反射
            //获取属性
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);

            preparedStatement.setObject(i+1, o);

        }
        return preparedStatement;
    }
}
