# springboot-multipledatasources


multipledatasources：sql 脚本

multipledatasources2：
通过分包实现多数据源的，但事务上存在问题，事务管理器只能处理主数据源的事务
0511 配置不同数据源各自的事务管理器，手动处理事务

multipledatasources3：
通过分包扫描，定义不同的两个数据库，利用jta-atomikos解决传统(单应用)项目多数据源事务管理问题

multipledatasources4：
通过aop的方式实现动态数据库切换+jta-atomikos(手动、注解自动处理多数据源事务问题)

multipledatasources5：
通过aop的方式实现动态数据库切换，事务管理器通过动态数据源创建，默认只处理主数据源的事务