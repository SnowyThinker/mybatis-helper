package io.github.snowythinker.mh.test.page;

import io.github.snowythinker.mh.page.PageQueryResponse;

import java.util.List;
import java.util.Map;

public interface StudentMapper {

	PageQueryResponse<Student> queryList(Map<String, Object> params);

	PageQueryResponse<Student> queryPageList(Map<String, Object> params);
	
	//Long queryTotalCount(Map<String, Object> params);
	
	void createTable();
	
	int insert(Student student);
}
