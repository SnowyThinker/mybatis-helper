package io.github.snowthinkder.mh.test.page;

import java.io.IOException;
import java.util.List;

import io.github.snowthinkder.mh.test.SqlSessionFactoryHelper;
import io.github.snowthinker.mh.page.PageQuery;
import io.github.snowthinker.mh.page.PageResult;

public class StudentService {

	public PageResult<Student> queryPageList(PageQuery pageQuery) throws IOException {
		StudentMapper studentMapper = SqlSessionFactoryHelper.getSqlSessionFactory().openSession().getMapper(StudentMapper.class);
		
		studentMapper.createTable();
		
		List<Student> dataList = studentMapper.queryPageList(pageQuery.asMap());
		Long totalCount = studentMapper.queryTotalCount(pageQuery.asMap());
		
		return new PageResult<>(dataList, totalCount, pageQuery);
	}
}
