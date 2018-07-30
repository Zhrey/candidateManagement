package com.ray.core.api.controller;

import com.ray.cloud.framework.base.dto.ResultDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 15:36 2018/7/26
 * @Modified By:
 */
@RestController
public class HomeController {

    @RequestMapping("toHomePage")
    public ResultDTO toHomePage() {
        return ResultDTO.success();
    }
}
