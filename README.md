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
    