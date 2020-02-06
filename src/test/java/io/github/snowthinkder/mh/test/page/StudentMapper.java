package io.github.snowthinkder.mh.test.page;

import java.util.List;

import io.github.snowthinker.mh.page.PageQueryRequest;

public interface StudentMapper {

	List<Student> queryPageList(PageQueryRequest pageQuery);
	
	Long queryTotalCount(PageQueryRequest pageQuery);
	
	void createTable();
	
	int insert(Student student);
}
