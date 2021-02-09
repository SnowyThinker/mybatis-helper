package io.github.snowythinker.mh.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PageResult<T> {

    @ApiModelProperty("×Ü¼ÇÂ¼Êý")
    private Long totalCount;

    @ApiModelProperty("Ã¿Ò³¼ÇÂ¼Êý")
    private Integer pageSize;

    @ApiModelProperty("×ÜÒ³Êý")
    private Integer totalPage;

    @ApiModelProperty("µ±Ç°Ò³Êý")
    private Integer currPage;

    @ApiModelProperty("ÁÐ±íÊý¾Ý")
    private List<T> list;

    /**
     * ·ÖÒ³
     * @param list        ÁÐ±íÊý¾Ý
     * @param totalCount  ×Ü¼ÇÂ¼Êý
     * @param pageSize    Ã¿Ò³¼ÇÂ¼Êý
     * @param currPage    µ±Ç°Ò³Êý
     */
    public PageResult(List<T> list, Long totalCount, Integer pageSize, Integer currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        if(totalCount % pageSize == 0) {
            this.totalPage = totalCount.intValue() / pageSize;
        } else {
            this.totalPage = totalCount.intValue() / pageSize + 1;
        }
    }
}


