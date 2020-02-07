package io.github.snowythinker.mh.page.dialect;

public class H2Dialect implements Dialect {

	@Override
	public String paginationSqlWrap(String originalSql, int currentPage, int pageSize) {
        int startRow = (currentPage - 1) * pageSize;
        StringBuffer sb = new StringBuffer();
        sb.append(originalSql).append(" limit ").append(startRow).append(",").append(pageSize);
		return sb.toString();
	}
}
