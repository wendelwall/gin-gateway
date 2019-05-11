package com.riskeys.gateway.model;

import com.riskeys.gateway.model.dto.GitListResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ：sunrise
 * @description ：
 * @copyright ：	Copyright 2019 yowits Corporation. All rights reserved.
 * @create ：2019/4/21 15:41
 */
@FeignClient(name = "route", url = "http://localhost:8086")
public interface GatewayRouteBiz {

    @GetMapping(value = "/route/list")
    List<GatewayRoute> selectListAll();

    @GetMapping(value = "/route/getList")
    GitListResult selectListAll(@RequestParam(value = "pageNum")Integer pageNum,
                                @RequestParam(value = "pageSize")Integer pageSize);

}
