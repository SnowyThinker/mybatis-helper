package io.github.snowythinker.mh.page.dialect;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import io.github.snowythinker.mh.page.DatabaseType;

public class DialectHelper {
	
	private static final Log logger = LogFactory.getLog(Dialect.class);
	
	static Map<String, DatabaseType> databaseDrivers = new HashMap<>();
	
	static {
		databaseDrivers.put("org.h2.Driver", DatabaseType.H2);
		databaseDrivers.put("com.mysql.jdbc.Driver", DatabaseType.MySQL);
		databaseDrivers.put("oracle.jdbc.driver.OracleDriver", DatabaseType.Oracle);
		databaseDrivers.put("com.microsoft.sqlserver.jdbc.SQLServerDriver", DatabaseType.SQLServer);
		databaseDrivers.put("org.mariadb.jdbc.Driver", DatabaseType.MariaDB);
	}
	
	public static DatabaseType recongnizeDbType(String driverClass) {
		if(null == driverClass || "".equals(driverClass)) {
			logger.error(String.format("Driver class %s can not be recongnized", driverClass));
			return null;
		}
		return databaseDrivers.get(driverClass.trim());
	}
}
