package io.github.snowythinker.mh.page.interceptor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import io.github.snowythinker.mh.page.DatabaseType;
import io.github.snowythinker.mh.page.dialect.Dialect;
import io.github.snowythinker.mh.page.dialect.H2Dialect;
import io.github.snowythinker.mh.page.dialect.MySQLDialect;
import io.github.snowythinker.mh.page.dialect.OracleDialect;
import io.github.snowythinker.mh.page.dialect.SQLServerDialect;
import io.github.snowythinker.mh.util.ExecutorUtil;
import io.github.snowythinker.mh.util.MappedStatementUtil;


@Intercepts({ 
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) 
})
public class PageInterceptor implements Interceptor{
	
	private static final Map<DatabaseType, Dialect> dialects = new HashMap<>();
	
	private static final Log logger = LogFactory.getLog(PageInterceptor.class);
	
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
		String originalSql = boundSql.getSql().trim().toLowerCase();
		Executor executor = (Executor) invocation.getTarget();
		
        if(originalSql.contains("count(")) {
        	return invocation.proceed();
        }
        
        Object parameterObject = boundSql.getParameterObject();
        Map<String, Object> queryPage = checkPaginationCondition(parameterObject);
        if(null == queryPage) {
        	return invocation.proceed();
        }
        
        DataSource dataSource = mappedStatement.getConfiguration().getEnvironment().getDataSource();
        Connection connection = dataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        String dbTypeName = metaData.getDatabaseProductName();
        
        DatabaseType databaseType = DatabaseType.valueOf(dbTypeName);
        Dialect dialect = dialects.get(databaseType);
        
        Long totalCount = this.invokeTotalCount(originalSql, executor, mappedStatement, boundSql, parameter);
        queryPage.put("totalCount", totalCount);
        
        Integer currentPage = (Integer) queryPage.get("currentPage");
    	Integer pageSize = (Integer) queryPage.get("pageSize");
        String wrappedSql = dialect.paginationSqlWrap(originalSql, currentPage, pageSize);
        
        BoundSql newBoundSql = MappedStatementUtil.copyFromBoundSql(mappedStatement, boundSql, wrappedSql);
        MappedStatement newMs = MappedStatementUtil.copyFromMappedStatement(mappedStatement,new BoundSqlSqlSource(newBoundSql));
        invocation.getArgs()[0]= newMs;
            
        return invocation.proceed();
    }

    private Long invokeTotalCount(String originalSql, Executor executor, MappedStatement mappedStatement, BoundSql boundSql, Object parameter) {
		String countSql = getCountSql(originalSql);
		BoundSql countBS = MappedStatementUtil.copyFromBoundSql(mappedStatement, boundSql, countSql);
		String countMsId = mappedStatement.getId() + "_count";
		
		final List<Long> countResultList = new ArrayList<Long>();
		ResultHandler<Long> resultHandler = new ResultHandler<Long>() {
			@Override
			public void handleResult(ResultContext<? extends Long> resultContext) {
				countResultList.add(resultContext.getResultObject());
			}
		};
		
		Long totalCount = 0L;
		MappedStatement countMs = ExecutorUtil.getExistedMappedStatement(mappedStatement.getConfiguration(), countMsId);
        if (countMs != null) {
        	try {
				totalCount = ExecutorUtil.executeManualCount(executor, countMs, parameter, boundSql, resultHandler, countResultList);
			} catch (SQLException e) {
				logger.error("Execute exists count from mapper error", e);
			}
        	return totalCount;
        } 
		
		countMs = MappedStatementUtil.newCountMappedStatement(mappedStatement, countMsId);
		CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, countBS);
		
		try {
			executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBS);
		} catch (SQLException e) {
			logger.error("Execute count error", e);
		}
		return countResultList.get(0);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> checkPaginationCondition(Object o) {
		if (o instanceof HashMap) {
			Map<String, Object> query = (Map<String, Object>) o;
			if(query.containsKey("currentPage") && query.containsKey("pageSize")) {
				Integer currentPage = (Integer) query.get("currentPage");
				Integer pageSize = (Integer) query.get("pageSize");
				if (null != currentPage && null != pageSize) {
					return query;
				}	
			}
		}
		return null;
	}


    private String getCountSql(String sql) {
        return "SELECT COUNT(*) FROM (" + sql + ") t";
    }

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
