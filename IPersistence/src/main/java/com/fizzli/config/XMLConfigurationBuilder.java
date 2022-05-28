package com.fizzli.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.fizzli.io.Resources;
import com.fizzli.pojo.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigurationBuilder {

    private Configuration configuration;

    public XMLConfigurationBuilder() {
        this.configuration = new Configuration();
    }


    //通过dom4j解析配置文件
    public Configuration parseConfig(InputStream in) throws DocumentException, PropertyVetoException {

        Document document = new SAXReader().read(in);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//property");
        //解析property标签中的属性信息
        Properties properties = new Properties();
        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name,value);
        }

        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driveClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(comboPooledDataSource);

       /* DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(properties.getProperty("driveClass"));
        druidDataSource.setUrl(properties.getProperty("jdbcUrl"));
        druidDataSource.setUsername(properties.getProperty("username"));
        druidDataSource.setPassword(properties.getProperty("password"));*/
        //configuration.setDataSource(druidDataSource);

        //解析mapper文件
        List<Element> mapperList = rootElement.selectNodes("//mapper");
        for (int i = 0; i < mapperList.size(); i++) {

            Element element = mapperList.get(i);
            String mapperPath = element.attributeValue("resource");
            InputStream resourcesAsStream = Resources.getResourcesAsStream(mapperPath);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            xmlMapperBuilder.parseMapper(resourcesAsStream);
        }
        return configuration;
    }
}
