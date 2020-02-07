package io.github.snowythinker.mh.test.page;

import java.util.List;

import io.github.snowythinker.mh.page.PageQueryRequest;

public interface StudentMapper {

	List<Student> queryPageList(PageQueryRequest pageQuery);
	
	Long queryTotalCount(PageQueryRequest pageQuery);
	
	void createTable();
	
	int insert(Student student);
}
