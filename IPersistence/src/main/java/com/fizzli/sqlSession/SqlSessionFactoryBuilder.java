package com.fizzli.sqlSession;

import com.fizzli.config.XMLConfigurationBuilder;
import com.fizzli.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {
    /*
    (1) 用来解析配置文件信息，将解析出来的内容封装到javaBean中
	(2) 创建SqlSessionFactory对象
     */
    public SqlSessionFactory build(InputStream in) throws DocumentException, PropertyVetoException {
        //1.解析配置文件封装到Configuration中
        XMLConfigurationBuilder xmlConfigurationBuilder = new XMLConfigurationBuilder();
        Configuration configuration = xmlConfigurationBuilder.parseConfig(in);

        //2.创建SqlSessionFactory
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);

        return  defaultSqlSessionFactory;
    }
}
