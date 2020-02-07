package io.github.snowythinker.mh.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public abstract class AdvancedPageQuery<T extends Object> {
	
	@NotNull(message="当前页数不能为空")
	@Range(min=1, message="请输入合法的当前页数")
	@ApiModelProperty(value="当前页数", required=true, example="1")
	private Integer currentPage;
	
	@NotNull(message="页数大小不能为空")
	@Range(min=1, max=500, message="请输入合法的页数大小")
	@ApiModelProperty(value="页数大小", required=true, example="10")
	private Integer pageSize;
	
	@ApiModelProperty(value="表单排序（排序字段: 排序方向） sendTime : desc", required=false)
	private List<PageQuerySort> sorts = new ArrayList<>();
    
	public abstract T getConditions();

	public abstract void setConditions(T conditions);
	
	public Map<String, Object> asMap() {
		Map<String, Object> rs = new HashMap<String, Object>();
		rs.put("pageSize", pageSize);
		rs.put("currentPage", currentPage);
		
		rs.putAll(PojoUtils this.getConditions());
		rs.put("sorts", hump2UnderLine(sorts));
		
		return rs;
	}
}
