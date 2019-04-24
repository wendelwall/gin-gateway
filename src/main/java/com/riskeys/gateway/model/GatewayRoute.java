package com.riskeys.gateway.model;

import lombok.Data;

/**
 * @author ：sunrise
 * @description ：
 * @copyright ：	Copyright 2019 yowits Corporation. All rights reserved.
 * @create ：2019/4/20 23:17
 */
@Data
public class GatewayRoute {
    //
    private String uuid;

    //映射路劲
    private String path;

    //映射服务
    private String serviceId;

    //映射外连接
    private String url;

    //令牌桶流速
    private String limiterRate;

    //令牌桶容量
    private String limiterCapacity;

    //是否启用
    private String enabled;

    //是否忽略前缀
    private Integer stripPrefix;

    private String routeOrder;
}
