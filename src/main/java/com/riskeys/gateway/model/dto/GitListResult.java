package com.riskeys.gateway.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author ：sunrise
 * @description ：
 * @copyright ：	Copyright 2019 yowits Corporation. All rights reserved.
 * @create ：2019/5/5 22:02
 */
@Data
public class GitListResult extends JsonResult<List<SysRouteConfVo>> {
    private Integer pageNum;

    private Integer pageSize;

    private Integer total;
}
