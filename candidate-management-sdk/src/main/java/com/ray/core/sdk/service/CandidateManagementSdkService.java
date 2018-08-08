package com.ray.core.sdk.service;

import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.core.sdk.dto.UserBaseDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 15:25 2018/7/26
 * @Modified By:
 */
@FeignClient(value = "candidate-management")
public interface CandidateManagementSdkService {

    @RequestMapping("login")
    ResultDTO login(UserBaseDTO userBaseDTO);

    @RequestMapping(value = "uploadFile")
    ResultDTO uploadFile(@RequestParam("fileModel") File file);

}
