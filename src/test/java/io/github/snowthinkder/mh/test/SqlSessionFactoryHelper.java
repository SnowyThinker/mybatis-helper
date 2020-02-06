package io.github.snowthinkder.mh.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionFactoryHelper {

	private static SqlSessionFactory sqlSessionFactory;
	
	private SqlSessionFactoryHelper() throws IOException {	
	}
	
	public static SqlSessionFactory getSqlSessionFactory() throws IOException {
		if(null != sqlSessionFactory) {
			return sqlSessionFactory;
		}
		
		String resource = "/mybatis.xml";
		InputStream inputStream = SqlSessionFactoryHelper.class.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSessionFactoryHelper.sqlSessionFactory = sqlSessionFactory;
		
		return sqlSessionFactory;
	}
}
