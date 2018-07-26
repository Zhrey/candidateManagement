package com.ray.core.sdk.service;

import com.ray.cloud.framework.base.dto.ResultDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 15:25 2018/7/26
 * @Modified By:
 */
@FeignClient(value = "candidate-management")
@RequestMapping("")
public interface CandidateManagementService {

    @RequestMapping("toHomePage")
    ResultDTO toHomePage();
}