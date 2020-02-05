package io.github.snowthinker.mh.page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import io.swagger.annotations.ApiModelProperty;

public abstract class AdvancedPageQuery<T extends Object> implements Serializable {
	
	@NotNull(message="当前页数不能为空")
	@Range(min=1, message="请输入合法的当前页数")
	@ApiModelProperty(value="当前页数", required=true, example="1")
	private Integer currentPage;
	
	@NotNull(message="页数大小不能为空")
	@Range(min=1, max=500, message="请输入合法的页数大小")
	@ApiModelProperty(value="页数大小", required=true, example="10")
	private Integer pageSize;
	
	@ApiModelProperty(value="表单排序（排序字段: 排序方向） sendTime : desc", required=false)
	private Map<String, Object> sorts = new HashMap<>();
    

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public abstract T getConditions();

	public abstract void setConditions(T conditions);

	public Map<String, Object> getSorts() {
		return sorts;
	}

	public void setSorts(Map<String, Object> sorts) {
		this.sorts = sorts;
	}
	
	public abstract Map<String, Object> asMap();
	
}
