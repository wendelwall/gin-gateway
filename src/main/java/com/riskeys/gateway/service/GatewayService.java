package com.riskeys.gateway.service;

import com.alibaba.fastjson.JSON;
import com.riskeys.gateway.config.RedisRouteDefinitionRepository;
import com.riskeys.gateway.model.GatewayRoute;
import com.riskeys.gateway.model.GatewayRouteBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：sunrise
 * @description ：
 * @copyright ：	Copyright 2019 yowits Corporation. All rights reserved.
 * @create ：2019/4/20 23:15
 */
@Slf4j
//@Service
public class GatewayService implements ApplicationEventPublisherAware, CommandLineRunner {
//    @Autowired
//    private RedisRouteDefinitionRepository routeDefinitionWriter;
    @Autowired
    private RedisRouteDefinitionRepository routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    //这里替换成你的路由service
    @Autowired
    private GatewayRouteBiz gatewayRouteBiz;


    public String save() {
        //从数据库拿到路由配置
        List<GatewayRoute> gatewayRouteList = gatewayRouteBiz.selectListAll();

        log.info("网关配置信息：=====>"+ JSON.toJSONString(gatewayRouteList));
        gatewayRouteList.forEach(gatewayRoute -> {
            RouteDefinition definition = new RouteDefinition();
            Map<String, String> predicateParams = new HashMap<>(8);
            PredicateDefinition predicate = new PredicateDefinition();
            FilterDefinition filterDefinition = new FilterDefinition();
            Map<String, String> filterParams = new HashMap<>(8);
            definition.setId(gatewayRoute.getUuid());
            predicate.setName("Path");
            predicateParams.put("pattern", "/api"+gatewayRoute.getPath());
            predicateParams.put("pathPattern", "/api"+gatewayRoute.getPath());
            URI uri = UriComponentsBuilder.fromUriString("lb://"+gatewayRoute.getServiceId()).build().toUri();
            filterDefinition.setName("StripPrefix");

            // 路径去前缀
            filterParams.put("_genkey_0", gatewayRoute.getStripPrefix().toString());
            // 令牌桶流速
            filterParams.put("redis-rate-limiter.replenishRate", gatewayRoute.getLimiterRate());
            //令牌桶容量
            filterParams.put("redis-rate-limiter.burstCapacity", gatewayRoute.getLimiterCapacity());
            // 限流策略(#{@BeanName})
            filterParams.put("key-resolver", "#{@remoteAddrKeyResolver}");

            predicate.setArgs(predicateParams);
            filterDefinition.setArgs(filterParams);


            definition.setPredicates(Arrays.asList(predicate));
            definition.setFilters(Arrays.asList(filterDefinition));
            definition.setUri(uri);
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        });

        this.publisher.publishEvent(new RefreshRoutesEvent(this));

        return "success";
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Override
    public void run(String... args){
        this.save();
    }


    public void deleteRoute(String routeId){
        routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
    }
}
