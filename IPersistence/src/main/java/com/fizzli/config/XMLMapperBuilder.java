package com.fizzli.config;

import com.fizzli.pojo.Configuration;
import com.fizzli.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {

    private Configuration configuration;
    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 解析mapper配置文件
     * @param resourcesAsStream
     * @throws DocumentException
     */
    public void parseMapper(InputStream resourcesAsStream) throws DocumentException {
        Document document = new SAXReader().read(resourcesAsStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");

        //这里只能解析select标签
        List<Element> selectList = rootElement.selectNodes("//select|//insert|//delete|//update");
        for (Element element : selectList) {
            String id = element.attributeValue("id");
            String parameterType = element.attributeValue("parameterType");
            String resultType = element.attributeValue("resultType");
            String sql = element.getTextTrim();

            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSql(sql);

            String key = namespace+"."+id;
            configuration.getMappedStatementMap().put(key,mappedStatement);
        }

    }
}
