###mybatis-helper

####如何使用
pom 引入jar包
~~~
<dependency>
	<groupId>io.github.snowthinker</groupId>
	<artifactId>mybatis-helper</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
~~~

编写枚举类
~~~
public enum StockMarket implements MyBatisEnum<StockMarket, Integer>{

	US,
	HK,
	CN;

	@Override
	public Integer getValue() {
		return this.ordinal();
	}
}
~~~

####字段枚举支持
mybatis.xml 添加typeHandler
~~~
<typeHandlers>
    <typeHandler handler="io.github.snowthinker.mh.EnumTypeHandler" javaType="io.github.snowthinkder.mh.test.UserStatus"/>
</typeHandlers>
~~~

####获取 mapper.xml 文件方法 SQL语句
~~~
String sql = MybatisSqlHelper.getMapperSql("oi.github.snowthinker.mapper.UserMapper", "queryById", 324);
System.out.println(sql);
~~~

<script type="text/javascript" src="https://snowthinker.github.io/reward.js"></script>
<!--
<div class='reward'>
	<div style='text-align: center; display: inline-block; '><img width='250' height='250' src='https://snowthinker.github.io/res/wechat_pay.jpeg'/> &nbsp; &nbsp;&nbsp; <img width='250' height='250' src='https://snowthinker.github.io/res/alipay.jpeg'/>
</div>
<div>
-->
