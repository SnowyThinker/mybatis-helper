package io.github.snowythinker.mh.test;

import io.github.snowythinker.mh.MyBatisEnum;

public enum UserStatus implements MyBatisEnum<UserStatus, Integer>{
	
    ACTIVE(1),
    
    LOCK(0);
	
	private Integer value;
	
    private UserStatus(Integer value) {
        this.value = value;
    }

	@Override
	public Integer getValue() {
		return value;
	}

}
