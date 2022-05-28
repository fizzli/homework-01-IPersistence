package com.fizzli.test;

import com.fizzli.dao.IUserDao;
import com.fizzli.io.Resources;
import com.fizzli.pojo.User;
import com.fizzli.sqlSession.SqlSession;
import com.fizzli.sqlSession.SqlSessionFactory;
import com.fizzli.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

//测试
public class IPersistenceTest {

    private  SqlSession sqlSession;


    @Before
    public void before () throws Exception {
        InputStream resourcesAsStream = Resources.getResourcesAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(resourcesAsStream);
        sqlSession = sqlSessionFactory.openSession();
    }

    @Test
    public void test() {
        IUserDao mapper = sqlSession.getMapper(IUserDao.class);

        User user = new User();
        user.setId(1);
        user.setUsername("lucy1");
        User userByCondition = mapper.findUserByCondition(user);
        System.out.println(userByCondition);

    }

    @Test
    public void test1() {
        IUserDao mapper = sqlSession.getMapper(IUserDao.class);
        List<User> all = mapper.findAll();
        all.forEach(System.out::println);

    }

    //新增
    @Test
    public void test2() {
        IUserDao mapper = sqlSession.getMapper(IUserDao.class);
        User user = new User();
        user.setId(3);
        user.setUsername("lisi");
        user.setPassword("1234");
        user.setBirthday("2000-02-02");
        int i = mapper.addUser(user);
        System.out.println(i);
    }

    //修改
    @Test
    public void test3() {
        IUserDao mapper = sqlSession.getMapper(IUserDao.class);
        User user = new User();
        user.setId(3);
        user.setUsername("wanger");
        user.setPassword("12345");
        user.setBirthday("2000-02-04");
        int i = mapper.updateUserById(user);
        System.out.println(i);
    }

    //删除
    @Test
    public void test4() {
        IUserDao mapper = sqlSession.getMapper(IUserDao.class);
        User user = new User();
        user.setId(3);
        int i = mapper.delUserById(user);
        System.out.println(i);
    }

}
