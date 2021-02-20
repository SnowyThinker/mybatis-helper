package io.github.snowythinker.mh.test.page;

import java.io.IOException;


import io.github.snowythinker.mh.page.PageQueryRequest;
import io.github.snowythinker.mh.page.PageQueryResponse;
import io.github.snowythinker.mh.page.PageQuerySort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StudentServiceTest {
	
	private static StudentService studentService;
	
	static {
		try {
			studentService = new StudentService();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@BeforeEach
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
		pageQuery.setCurrentPage(1);
//		pageQuery.getConditions().put("name", "Andrew");
//		pageQuery.getConditions().put("passportNumber", "OSI-9002332");
//
//		pageQuery.getSorts().add(new PageQuerySort("name", "desc"));
//		pageQuery.getSorts().add(new PageQuerySort("passportNumber", "desc"));
		
		PageQueryResponse<Student> pageResult = studentService.queryPageList(pageQuery);
		System.out.println(pageResult);
	}

	@Test
	public void testQueryList(){
		PageQueryResponse<Student> pageResp = studentService.queryList();
		System.out.println(pageResp);
	}
}
