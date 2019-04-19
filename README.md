###

####如何使用
pom 引入jar包
~~~
<dependency>
	<groupId>io.github.snowthinker</groupId>
	<artifactId>mybatis-helper</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
~~~

mybatis.xml 添加typeHandler
~~~
<typeHandlers>
    <typeHandler handler="io.github.snowthinker.mh.EnumTypeHandler" javaType="io.github.snowthinkder.mh.test.UserStatus"/>
</typeHandlers>
~~~

