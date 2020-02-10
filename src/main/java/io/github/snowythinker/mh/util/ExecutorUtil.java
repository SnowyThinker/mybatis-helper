package io.github.snowythinker.mh.util;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;


public abstract class ExecutorUtil {

    private static Field additionalParametersField;

    static {
        try {
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("获取 BoundSql 属性 additionalParameters 失败: " + e, e);
        }
    }

    /**
     * <p>尝试获取已经存在的在 MS，提供对手写count和page的支持
     * @param configuration The configuration
     * @param msId The MappedStatement Id
     * @return MappedStatement 
     */
    public static MappedStatement getExistedMappedStatement(Configuration configuration, String msId) {
        MappedStatement mappedStatement = null;
        try {
            mappedStatement = configuration.getMappedStatement("queryPageCount", false);
        } catch (Throwable t) {
            //ignore
        }
        return mappedStatement;
    }

    /**
     * <p>执行手动设置的 count 查询，该查询支持的参数必须和被分页的方法相同
     * @param executor The Executor
     * @param countMs The count MappedStatement
     * @param parameter The parameter
     * @param boundSql The boundSQL
     * @param resultHandler The result handler
     * @param countResultList The count result list
     * @return Long
     * @throws SQLException
     */
	public static Long executeManualCount(Executor executor, MappedStatement countMs,
                                          Object parameter, BoundSql boundSql,
                                          ResultHandler<Long> resultHandler, 
                                          List<Long> countResultList) throws SQLException {
        CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, boundSql);
        BoundSql countBoundSql = countMs.getBoundSql(parameter);
        executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        return countResultList.get(0);
    }
}
