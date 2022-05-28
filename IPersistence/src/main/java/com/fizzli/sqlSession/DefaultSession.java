package com.fizzli.sqlSession;

import com.fizzli.pojo.Configuration;
import com.fizzli.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;

public class DefaultSession implements SqlSession {

    private Configuration configuration;

    public DefaultSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        //完成对SimepleExecutor中的query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        List<Object> list = simpleExecutor.query(configuration, mappedStatement, params);

        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if (objects.size() == 1){
            return (T) objects.get(0);
        }else{
           throw new RuntimeException("查询结果空，或者结果过多");
        }
    }

    @Override
    public int executeUpdate(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        int result  = simpleExecutor.executeUpdate(configuration,mappedStatement,params);
        return result;
    }


    /**
     * 采用动态代理的方式动态调用相关方法
     * @param tClass
     * @return
     * @param <T>
     */
    @Override
    public <T> T getMapper(Class<T> tClass) {

        Object proxyInstance = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{tClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String className = method.getDeclaringClass().getName();
                String methodName = method.getName();
                String statementId = className + "." + methodName;

                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType){
                    List<Object> list = selectList(statementId, args);
                    return list;
                }
                Class<?> returnType = method.getReturnType();
                if (returnType.isAssignableFrom(int.class)){
                    return executeUpdate(statementId, args);
                }

                return selectOne(statementId, args);

            }
        });
        return (T) proxyInstance;
    }
}
