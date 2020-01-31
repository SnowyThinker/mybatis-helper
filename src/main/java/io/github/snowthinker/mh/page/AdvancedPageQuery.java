package io.github.snowthinker.mh.page;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import io.swagger.annotations.ApiModelProperty;

public abstract class AdvancedPageQuery<T extends Object> {
	
	private static Pattern humpPattern = Pattern.compile("[A-Z]");

	@NotNull(message = "current page can not be null")
	@Range(min=1, message = "page value must greater than 0")
	@ApiModelProperty(value="Current page number", required=true, example="1")
	private Integer currentPage;
	
	@NotNull(message = "Page size can not be null")
	@Range(min=1, max=1000, message = "page size must greater than 0")
	@ApiModelProperty(value="Current page number", required=true, example="10")
	private Integer pageSize;
	
	@ApiModelProperty(value="Sort columns and direction", required=true, example="{\"name\":\"desc\"}")
	private Map<String, Object> sorts = new LinkedHashMap<>();
	
	public abstract T getConditions();
	
	public abstract void setConditions(T conditions);
	
	public static String hump2Line(String property) {
		Matcher matcher = humpPattern.matcher(property);
		StringBuffer sb = new StringBuffer();
	}
}
