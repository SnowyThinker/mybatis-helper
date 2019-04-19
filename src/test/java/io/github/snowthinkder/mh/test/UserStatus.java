package io.github.snowthinkder.mh.test;

import io.github.snowthinker.mh.MyBatisEnum;

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
