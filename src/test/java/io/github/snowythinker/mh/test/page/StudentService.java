package io.github.snowythinker.mh.test.page;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.session.SqlSessionFactory;

import io.github.snowythinker.mh.page.PageQueryRequest;
import io.github.snowythinker.mh.page.PageQueryResponse;
import io.github.snowythinker.mh.test.SqlSessionFactoryHelper;

public class StudentService {
	
	private StudentMapper studentMapper;
	
	private SqlSessionFactory sqlSessionFactory;
	
	public StudentService() throws IOException {
		studentMapper = SqlSessionFactoryHelper.getSqlSessionFactory().openSession().getMapper(StudentMapper.class);
		sqlSessionFactory = SqlSessionFactoryHelper.getSqlSessionFactory();
	}
	
	public void createTable() {
		studentMapper.createTable();
	}
	
	public void batchInsert() {
		for(int i=0; i<1000; i++) {
			Student student = new Student();
			student.setId(UUID.randomUUID().toString());
			student.setName(RandomStringUtils.randomAscii(5, 10));
			student.setPassportNumber(RandomStringUtils.randomAlphanumeric(10, 20));
			studentMapper.insert(student);	
			
			Student student2 = new Student();
			student2.setId(UUID.randomUUID().toString());
			student2.setName("Andrew");
			student2.setPassportNumber("OSI-9002332");
			studentMapper.insert(student2);	
		}
		
		sqlSessionFactory.openSession().commit();
	}

	public PageQueryResponse<Student> queryPageList(PageQueryRequest pageQuery) throws IOException {

		PageQueryResponse<Student> pageQueryResponse = studentMapper.queryPageList(pageQuery.asMap());
		return pageQueryResponse;
		//Long totalCount = 0L;//(Long) params.get("totalCount");
		
		//return new PageQueryResponse<>(dataList, totalCount, pageQuery);
	}

	public PageQueryResponse<Student> queryList() {
		return studentMapper.queryList(new HashMap<>());
	}
}
