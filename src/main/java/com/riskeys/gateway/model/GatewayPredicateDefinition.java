package com.riskeys.gateway.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author:xianxiong
 * @Date: Create in 16:31 2019/3/29 0029
 *
 * 路由断言模型
 */
public class GatewayPredicateDefinition {

    //断言对应的Name
    private String name;
    //配置的断言规则
    private Map<String, String> args = new LinkedHashMap<>();
}
