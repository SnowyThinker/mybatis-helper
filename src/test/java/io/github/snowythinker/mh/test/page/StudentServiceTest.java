package io.github.snowythinker.mh.test.page;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import io.github.snowythinker.mh.page.PageQueryRequest;
import io.github.snowythinker.mh.page.PageQueryResponse;
import io.github.snowythinker.mh.page.PageQuerySort;

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
	public void testBatchInsert() {
		studentService.batchInsert();
	}

	@Test
	public void testQueryPage() throws IOException {
		PageQueryRequest pageQuery = new PageQueryRequest();
		pageQuery.setPageSize(10);
		pageQuery.setCurrentPage(2);
		pageQuery.getConditions().put("name", "Andrew");
		pageQuery.getConditions().put("passportNumber", "OSI-9002332");
		
		pageQuery.getSorts().add(new PageQuerySort("name", "desc"));
		pageQuery.getSorts().add(new PageQuerySort("passportNumber", "desc"));
		
		PageQueryResponse<Student> pageResult = studentService.queryPageList(pageQuery);
		System.out.println(pageResult);
	}
}
