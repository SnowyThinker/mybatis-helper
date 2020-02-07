package io.github.snowythinker.mh;

public interface MyBatisEnum<E extends Enum<?>, T> {
    
	/**
     * 获取枚举的值
     * @return 枚举的值
     */
    T getValue();
}
