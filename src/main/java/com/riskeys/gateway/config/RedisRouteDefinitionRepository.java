package com.riskeys.gateway.config;

import com.alibaba.fastjson.JSON;
import com.riskeys.gateway.utils.NsMapCache;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：sunrise
 * @description ：将定义好的路由表信息通过此类读写到redis中
 * @copyright ：	Copyright 2019 yowits Corporation. All rights reserved.
 * @create ：2019/4/20 22:42
 */
//@Component
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {
    public static final String GATEWAY_ROUTES = "geteway:routes";

    @Resource
    private StringRedisTemplate redisTemplate;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        //先从ConcurrentHashMap本地缓存取一份；没有再到redis缓存中拿。
        String value =  NsMapCache.getCache(GATEWAY_ROUTES);
        if(value==null){
            List<RouteDefinition> finalRouteDefinitions = routeDefinitions;
            redisTemplate.opsForHash().values(GATEWAY_ROUTES).stream().forEach(routeDefinition -> {
                finalRouteDefinitions.add(JSON.parseObject(routeDefinition.toString(), RouteDefinition.class));
            });
            NsMapCache.initCache(GATEWAY_ROUTES,JSON.toJSONString(routeDefinitions));
        }else {
            routeDefinitions =  JSON.parseArray(value, RouteDefinition.class);
        }
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route
                .flatMap(routeDefinition -> {
                    redisTemplate.opsForHash().put(GATEWAY_ROUTES, routeDefinition.getId(),
                            JSON.toJSONString(routeDefinition));
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (redisTemplate.opsForHash().hasKey(GATEWAY_ROUTES, id)) {
                redisTemplate.opsForHash().delete(GATEWAY_ROUTES, id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("路由文件没有找到: " + routeId)));
        });
    }
}
