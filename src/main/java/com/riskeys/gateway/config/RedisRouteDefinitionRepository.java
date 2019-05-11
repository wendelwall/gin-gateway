package com.riskeys.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:xianxiong
 * @Date: Create in 15:49 2019/3/29 0029
 *
 * 动态路由实现服务
 */
@Component
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {

    private static final Logger log = LoggerFactory.getLogger(RedisRouteDefinitionRepository.class);

    @Autowired
    private RedisTemplate redisTemplate;

    private String ROUTE_KEY = "gateway_route_key";


    /**
     * 此监听方法为路由修改使用(即增加，修改，删除路由后，点击“应用” 使用)
     * @param str1
     * @throws ClassNotFoundException
     */
//    @RabbitListener(queues = "topic.update")
    @KafkaListener(topics = "topic.update")
    public void update(String str1) throws ClassNotFoundException {
        System.out.println(Thread.currentThread().getName()+"接收到来自topic.update队列的消息: "+str1);
        RedisRouteDefinitionRepository.this.getRouteDefinitions();
        log.info("更新路由成功！");
    }

    /**
     * 配置服务启动时，发送消息到此队列
     * @param str1
     * @throws ClassNotFoundException
     */
//    @RabbitListener(queues = "topic.send")
    @KafkaListener(topics = "topic.send")
    public void send(String str1) throws ClassNotFoundException {
        System.out.println(Thread.currentThread().getName()+"接收到来自topic.send队列的消息: "+str1);
        RedisRouteDefinitionRepository.this.getRouteDefinitions();
        log.info("初始化路由成功！");
    }



    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RouteDefinition.class));
        List<RouteDefinition> values = redisTemplate.opsForHash().values(ROUTE_KEY);
        List<RouteDefinition> definitionList = new ArrayList<>();
        values.forEach(vo ->{
            RouteDefinition route = new RouteDefinition();
            BeanUtils.copyProperties(vo,route);
            definitionList.add(vo);
        });
        log.info("redis 中路由定义条数为：{},{}",definitionList.size(),definitionList);
        return Flux.fromIterable(definitionList);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            RouteDefinition routeDefinition = new RouteDefinition();
            BeanUtils.copyProperties(r,routeDefinition);
            log.info("保存路由信息{}",routeDefinition);
            redisTemplate.opsForHash().put(ROUTE_KEY,routeDefinition.getId(),routeDefinition);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        routeId.subscribe(id -> {
            log.info("删除路由的id为：{}",id);
            redisTemplate.opsForHash().delete(ROUTE_KEY,id);
        });
        return Mono.empty();
    }


}
