package io.github.snowythinker.mh.page.interceptor;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

public interface CustomInterceptor extends Interceptor {

    /**
     * 前置拦截
     * @param invocation 上下文
     */
    void before(Invocation invocation);

    /**
     * 后置拦截
     * @param invocation 上下文
     * @param result 执行结果
     */
    void after(Invocation invocation, Object result);

}
