package io.github.snowythinker.mh.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageQueryResponse<T> {
	
	@ApiModelProperty("总记录数")
	private Long totalCount;

	@ApiModelProperty("每页记录数")
	private Integer pageSize;

	@ApiModelProperty("总页数")
	private Integer totalPage;

	@ApiModelProperty("当前页数")
	private Integer currentPage;

	@ApiModelProperty("列表数据")
	private List<T> list;
	
	@ApiModelProperty("其它数据")
	private Map<String, ?> additional = new HashMap<>();
	
	public PageQueryResponse(List<T> list, Long totalCount, Integer pageSize, Integer currentPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		if(null != totalCount && null != pageSize) {
			this.totalPage = (int)Math.ceil((double)totalCount/pageSize);	
		}
	}
	
	public PageQueryResponse(List<T> list, Long totalCount, PageQueryRequest pageQuery) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageQuery.getPageSize();
		this.currentPage = pageQuery.getCurrentPage();
		if(null != totalCount && null != pageQuery && null != pageSize) {
			this.totalPage = (int)Math.ceil((double)totalCount/pageSize);	
		}
	}

	@Override
	public String toString() {
		return "PageResult [totalCount=" + totalCount + ", pageSize=" + pageSize + ", totalPage=" + totalPage
				+ ", currentPage=" + currentPage + ", list=" + (null == list ? 0 : list.size()) + ", additional=" + additional + "]";
	}
}
