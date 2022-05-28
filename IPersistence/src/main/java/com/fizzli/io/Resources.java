package com.fizzli.io;

import java.io.InputStream;

/**
 * 定义一个静态方法，根据配置文件的路径将泪痣文件加载成自己输入流，存储在内存中
 */
public class Resources {

    //根据配置文件的路径将泪痣文件加载成自己输入流，存储在内存中
    public static InputStream getResourcesAsStream(String path){
        InputStream resourceAsStream = Resources.class.getClassLoader().getResourceAsStream(path);
        return resourceAsStream;
    }
}
