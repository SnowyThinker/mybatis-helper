package io.github.snowythinker.mh.page.dialect;

public interface Dialect {
	
	String paginationSqlWrap(String originalSql, int currentPage, int pageSize);
}
