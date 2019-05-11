package com.riskeys.gateway.model.dto;

import lombok.Data;

/**
 * @author ：sunrise
 * @description ：
 * @copyright ：	Copyright 2019 yowits Corporation. All rights reserved.
 * @create ：2019/5/5 22:01
 */
@Data
public class JsonResult<T> {
    private Integer code;

    private String message;

    private T data;
}
