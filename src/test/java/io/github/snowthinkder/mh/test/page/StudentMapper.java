package io.github.snowthinkder.mh.test.page;

import java.util.List;
import java.util.Map;

public interface StudentMapper {

	List<Student> queryPageList(Map<String, Object> params);
	
	Long queryTotalCount(Map<String, Object> params);
	
	void createTable();
	
	int insert(Student student);
}
