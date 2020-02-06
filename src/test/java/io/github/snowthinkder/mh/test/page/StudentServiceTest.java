package io.github.snowthinkder.mh.test.page;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import io.github.snowthinker.mh.page.PageQuery;
import io.github.snowthinker.mh.page.PageResult;

public class StudentServiceTest {
	
	private static StudentService studentService;
	
	static {
		try {
			studentService = new StudentService();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void testSetup() {
		studentService.createTable();
		studentService.batchInsert();
	}

	@Test
	public void testQueryPage() throws IOException {
		//StudentService studentService = new StudentService();
		
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPageSize(10);
		pageQuery.setCurrentPage(1);
		
		PageResult<Student> pageResult = studentService.queryPageList(pageQuery);
		System.out.println(pageResult);
	}
}
