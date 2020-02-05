package io.github.snowthinker.mh.page;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	//当前页码
    private int page;
    //每页条数
    private int limit;
    //总条数
    private int total;
    
    private List<PageSort> sorts = new ArrayList<>();
    
    public List<PageSort> getSorts() {
		return sorts;
	}

	public void setSorts(List<PageSort> sorts) {
		this.sorts = sorts;
	}

	public Query() {}

	public Query(Map<String, Object> params){
        this.putAll(params);

        //分页参数
        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        this.put("offset", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);
        
        if(null != sorts && !sorts.isEmpty()) {
        	this.put("sort", sorts);	
        }
        
    }

    public void isPaging(boolean bool){
        if(bool){
            this.put("paging",this);
        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
