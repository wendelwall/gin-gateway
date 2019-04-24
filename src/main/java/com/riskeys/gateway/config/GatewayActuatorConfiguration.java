package com.riskeys.gateway.config;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.cloud.gateway.actuate.GatewayControllerEndpoint;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author ：sunrise
 * @description ：动态路由配置
 * @copyright ：	Copyright 2019 yowits Corporation. All rights reserved.
 * @create ：2019/4/20 22:48
 */
//@Configuration
//@ConditionalOnClass(Health.class)
public class GatewayActuatorConfiguration {
    @Bean
    @ConditionalOnEnabledEndpoint
    public GatewayControllerEndpoint gatewayControllerEndpoint(RouteDefinitionLocator routeDefinitionLocator, List<GlobalFilter> globalFilters,
                                                               List<GatewayFilterFactory> GatewayFilters, RouteDefinitionWriter routeDefinitionWriter,
                                                               RouteLocator routeLocator) {
        return new GatewayControllerEndpoint(routeDefinitionLocator, globalFilters, GatewayFilters, routeDefinitionWriter, routeLocator);
    }
}
