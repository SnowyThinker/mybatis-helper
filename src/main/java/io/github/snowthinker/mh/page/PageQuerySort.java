package io.github.snowthinker.mh.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageQuerySort {

	private String orderBy;
	
	private String direction = "asc";
}
