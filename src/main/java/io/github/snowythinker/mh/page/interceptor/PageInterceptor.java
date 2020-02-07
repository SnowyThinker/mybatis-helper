package io.github.snowythinker.mh.page.interceptor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

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

import io.github.snowythinker.mh.page.DatabaseType;
import io.github.snowythinker.mh.page.PageQueryRequest;
import io.github.snowythinker.mh.page.dialect.Dialect;
import io.github.snowythinker.mh.page.dialect.H2Dialect;
import io.github.snowythinker.mh.page.dialect.MySQLDialect;
import io.github.snowythinker.mh.page.dialect.OracleDialect;
import io.github.snowythinker.mh.page.dialect.SQLServerDialect;


@Intercepts({ 
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) 
})
public class PageInterceptor implements Interceptor{
	
	private static final Map<DatabaseType, Dialect> dialects = new HashMap<>();
	
	//private static final Log logger = LogFactory.getLog(PageInterceptor.class);
	
	static {
		dialects.put(DatabaseType.H2, new H2Dialect());
		dialects.put(DatabaseType.MariaDB, new MySQLDialect());
		dialects.put(DatabaseType.MySQL, new MySQLDialect());
		dialects.put(DatabaseType.Oracle, new OracleDialect());
		dialects.put(DatabaseType.SQLServer, new SQLServerDialect());
	}

    public Object intercept(Invocation invocation) throws Throwable {
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
        
        DataSource dataSource = mappedStatement.getConfiguration().getEnvironment().getDataSource();
        Connection connection = dataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        String dbTypeName = metaData.getDatabaseProductName();
        
        DatabaseType databaseType = DatabaseType.valueOf(dbTypeName);
        Dialect dialect = dialects.get(databaseType);
        
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
        
        String wrappedSql = dialect.paginationSqlWrap(originalSql, queryPage.getCurrentPage(), queryPage.getPageSize());
        
        BoundSql newBoundSql = copyFromBoundSql(mappedStatement, boundSql, wrappedSql);
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
