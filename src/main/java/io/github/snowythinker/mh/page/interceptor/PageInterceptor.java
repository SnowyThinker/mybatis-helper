package io.github.snowythinker.mh.page.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import io.github.snowythinker.mh.page.PageQueryRequest;


@Intercepts({ 
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) 
})
public class PageInterceptor implements Interceptor{

    public Object intercept(Invocation invocation) throws Throwable {
        //当前环境 MappedStatement，BoundSql，及sql取得
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = invocation.getArgs()[1];
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		String originalSql = boundSql.getSql().trim().toUpperCase();
        
        if(originalSql.contains("COUNT(")) {
        	return invocation.proceed();
        }
        
        Object parameterObject = boundSql.getParameterObject();
        PageQueryRequest queryPage = searchPageWithXpath(parameterObject);
        if(null == queryPage) {
        	return invocation.proceed();
        }
        
        //Page对象存在的场合，开始分页处理
        /*String countSql = getCountSql(originalSql);
        Connection connection=mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
        PreparedStatement countStmt = connection.prepareStatement(countSql);
        BoundSql countBS = copyFromBoundSql(mappedStatement, boundSql, countSql);
        DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, countBS);
        parameterHandler.setParameters(countStmt);
        ResultSet rs = countStmt.executeQuery();
        Long totalCount = 0L;
        if (rs.next()) {
        	totalCount = rs.getLong(1);
        }
        rs.close();
        countStmt.close();
        connection.close();
        queryPage.setTotalCount(totalCount);*/

        //对原始Sql追加limit
        int pageSize = queryPage.getPageSize();
        int startRow = (queryPage.getCurrentPage() - 1) * pageSize;
        //int endRow = queryPage.getCurrentPage() * pageSize;
        StringBuffer sb = new StringBuffer();
        sb.append(originalSql).append(" limit ").append(startRow).append(",").append(pageSize);
        BoundSql newBoundSql = copyFromBoundSql(mappedStatement, boundSql, sb.toString());
        MappedStatement newMs = copyFromMappedStatement(mappedStatement,new BoundSqlSqlSource(newBoundSql));
        invocation.getArgs()[0]= newMs;
            
        return invocation.proceed();
    }

    private PageQueryRequest searchPageWithXpath(Object o) {
        if (o instanceof PageQueryRequest) {
        	PageQueryRequest query = (PageQueryRequest) o;
            if(null != query.getCurrentPage() && null != query.getPageSize()) {
            	return query;
            }
        }
       return null;
    }

    /**
     * 复制MappedStatement对象
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms,SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(),ms.getId(),newSqlSource,ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        //builder.key(ms.getK());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    /**
     * 复制BoundSql对象
     */
    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    /**
     * 根据原Sql语句获取对应的查询总记录数的Sql语句
     */
    /*private String getCountSql(String sql) {
        return "SELECT COUNT(*) FROM (" + sql + ") t";
    }*/

    public class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;
        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }
    public void setProperties(Properties arg0) {
    }
}
