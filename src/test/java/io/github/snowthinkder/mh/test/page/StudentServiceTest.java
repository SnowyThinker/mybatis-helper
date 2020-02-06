package io.github.snowthinkder.mh.test.page;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import io.github.snowthinkder.mh.test.SqlSessionFactoryHelper;
import io.github.snowthinker.mh.page.PageQuery;
import io.github.snowthinker.mh.page.PageResult;

public class StudentServiceTest {
	
	/*@Before
	public void testSetup() {
	}*/

	@Test
	public void testQueryPage() throws IOException {
		StudentService studentService = new StudentService();
		
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPageSize(10);
		pageQuery.setCurrentPage(1);
		
		PageResult<Student> pageResult = studentService.queryPageList(pageQuery);
		System.out.println(pageResult);
	}
}
