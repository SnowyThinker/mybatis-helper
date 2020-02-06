package io.github.snowthinker.mh.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PageResult<T> {
	
	@ApiModelProperty("总记录数")
	private long totalCount;

	@ApiModelProperty("每页记录数")
	private int pageSize;

	@ApiModelProperty("总页数")
	private int totalPage;

	@ApiModelProperty("当前页数")
	private int currentPage;

	@ApiModelProperty("列表数据")
	private List<T> list;
	
	@ApiModelProperty("其它数据")
	private Map<String, ?> additional = new HashMap<>();
	
	public PageResult(List<T> list, long totalCount, int pageSize, int currentPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}
	
	public PageResult(List<T> list, long totalCount, PageQuery pageQuery) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageQuery.getPageSize();
		this.currentPage = pageQuery.getCurrentPage();
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}
}
