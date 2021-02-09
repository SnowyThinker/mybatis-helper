package io.github.snowythinker.mh.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class SimplePageQuery<T extends Object> {

    @NotNull(message="当前页数不能为空")
    @Range(min=1, message="请输入合法的当前页数")
    @ApiModelProperty(value="当前页数", required=true, example="1")
    private Integer currentPage;

    @NotNull(message="页数大小不能为空")
    @Range(min=1, max=500, message="请输入合法的页数大小")
    @ApiModelProperty(value="页数大小", required=true, example="10")
    private Integer pageSize;

    @ApiModelProperty(value="表单排序（排序字段: 排序方向） sendTime : desc")
    private List<PageQuerySort> sorts = new ArrayList<>();

    @ApiModelProperty(value="conditions")
    private Map<String, Object> conditions = new HashMap<>();


    public Map<String, Object> toMap() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.putAll(conditions);
        dataMap.put("beginRow", this.getBeginRow());
        dataMap.put("pageSize", this.pageSize);
        return dataMap;
    }

    public Long getBeginRow() {
        return null != this.currentPage && null != this.pageSize ? new Long((this.currentPage - 1) * this.pageSize) : 0L;
    }
}

