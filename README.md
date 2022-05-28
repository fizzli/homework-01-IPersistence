# homework-01-IPersistence
自定义持久层框架及使用端测试代码

## 视频讲解地址：
  链接：https://pan.baidu.com/s/1tStFZWHMODqoHKc2OtZP1Q?pwd=lzqf 
提取码：lzqf 


## 自定义一个持久层框架的设计思路：

### 使用端： 使用的时候引入框架的jar包
需要两个配置类文件：<br/>
	1.sqlMapConfig.xml 用来存放数据库配置相关的信息 <br/>
	2.mapper.xml       用来存款sql配置信息


### 自定义持久化框架：本质就是封装JDBC，简化使用端代码的编写

1.读取配置文件的内容：根据配置文件的路径，加载配置文件称为字节流，存储到内存中<br/>
	创建Resources类 方法：InputStream getResourceAsStream(String path) 

2.创建两个JavaBean用来存储配置文件中的信息<br/>
	Configuration：存放sqlMapConfig.xml解析出来的信息<br/>
	MappedStatement：存放mapper.xml 解析出来的xinx

3.解析配置文件：dom4j<br/>
	创建SqlSessionFactoryBuilder类 方法：build(InputStream in) <br/>
	(1) 用来解析配置文件信息，将解析出来的内容封装到javaBean中<br/>
	(2) 创建SqlSessionFactory对象，返回

4.创建SqlSessionFactory接口及默认实现类DefaultSqlSessionFactory<br/>
	调用openSession()方法，=创建SqlSession对象

5.创建SqlSession接口及默认实现类DefaultSqlSession<br/>
	定义对数据库的crud操作

6.创建Executor接口及实现类SimpleExecutor<br/>
	query(Configuration,MappedStatement,Object...params):执行JDBC代码
