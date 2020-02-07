package io.github.snowythinker.mh.test.page;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import io.github.snowythinker.mh.page.PageQueryRequest;
import io.github.snowythinker.mh.page.PageQueryResponse;
import io.github.snowythinker.mh.test.SqlSessionFactoryHelper;

public class StudentService {
	
	private StudentMapper studentMapper;
	
	public StudentService() throws IOException {
		studentMapper = SqlSessionFactoryHelper.getSqlSessionFactory().openSession().getMapper(StudentMapper.class);
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
		}
	}

	public PageQueryResponse<Student> queryPageList(PageQueryRequest pageQuery) throws IOException {
		List<Student> dataList = studentMapper.queryPageList(pageQuery);
		Long totalCount = studentMapper.queryTotalCount(pageQuery);
		
		return new PageQueryResponse<>(dataList, totalCount, pageQuery);
	}
}
