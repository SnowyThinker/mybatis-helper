package io.github.snowthinker.mh.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author Nicholas
 * 2019年4月11日 下午2:59:41
 */
@SuppressWarnings("serial")
@Data
public class PageQuery implements Serializable {
	
	private static Pattern humpPattern = Pattern.compile("[A-Z]");	
	
	@NotNull(message="当前页数不能为空")
	@Range(min=1, message="请输入合法的当前页数")
	@ApiModelProperty(value="当前页数", required=true, example="1")
	private Integer currentPage;
	
	@NotNull(message="页数大小不能为空")
	@Range(min=1, max=500, message="请输入合法的页数大小")
	@ApiModelProperty(value="页数大小", required=true, example="10")
	private Integer pageSize;
	
	@ApiModelProperty(value="表单条件", required=false)
	private Map<String, Object> conditions = new HashMap<>();
	
	@ApiModelProperty(value="表单排序", required=false)
	private List<PageSort> sorts = new ArrayList<>();

	public Map<String, Object> asMap() {
		Map<String, Object> rs = new HashMap<String, Object>();
		rs.put("pageSize", pageSize);
		rs.put("currentPage", currentPage);
		rs.put("paging", true);
		
		rs.putAll(this.conditions);
		rs.putAll(this.sorts);
		rs.put("_sorts", hump2UnderLine(this.sorts,rs));
		
		return rs;
	}
	
	/**
	 * @param sorts2
	 * @return
	 */
	private Map<? extends String, ? extends Object> hump2UnderLine(Map<String, Object> sorts,Map<String, Object> rs) {
		if(null == sorts || sorts.isEmpty()) {
			return null;
		}
		
		Map<String, Object> rsMap = new LinkedHashMap<>();
		
		for (Iterator<String> iter = sorts.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			String newKey = hump2Line(key);
			rsMap.put(newKey, sorts.get(key));
		}
		return rsMap;
	}

	
	public static String hump2Line(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
