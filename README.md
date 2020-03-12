# Spring-Cloud-Study
用于SpringCloud的学习

#config
    配置中心，用于读取远程仓库配置，并且通过Spring Cloud Bus进行将配置信息更新到对应的客户端
   
#config-client
    配置中心客户端，提供point-end接受配置刷新请求

#eureka-server
    注册中间，系统中所有服务都注册到该服务中
   
#feign
    一个RESTFULL客户端，提供了接口的实现，通过feign访问在eureka中注册的服务。
    
#hystrix
    spring cloud 中的熔断器，用于当服务无法响应是提供一个备用接口，反正出现服务雪崩故障。
    
#zuul
    spring cloud中的网关
    
#JPA
    hibernate中jpa插件，用于维护javabean之间的关联关系，达到动态构建SQL的目的

#JConsole
    用于学习JDK中各种工具的使用
    
#threadExample
    测试多线程并发可能会遇到的各种问题，如：线程池中使用ThreadLocal历史数据遗留问题，ConcurrentHashMap中需要注意并非该
    类中的操作都是线程安全的。CopyOnWriteArrayList与ArrayList在并行转串行运行时读写性能对比，本项目使用了JMH进行压力测试
    ，运行压测可以使用IDEA中JMH Plugin，或者手动编写main方法启动。其中有-rf json -rff result.json vm参数指定输出日志到result.json文件中，https://jmh.morethan.io/