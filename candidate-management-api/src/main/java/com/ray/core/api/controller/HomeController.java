package com.ray.core.api.controller;

import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.base.dto.ResultError;
import com.ray.cloud.framework.mybatis.entity.DUser;
import com.ray.core.api.dto.UserBaseDTO;
import com.ray.core.api.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 15:36 2018/7/26
 * @Modified By:
 */
@RestController
public class HomeController {

    @Autowired
    private HomeService homeService;

    @RequestMapping("login")
    public ResultDTO login(@RequestBody UserBaseDTO userBaseDTO) {

        ResultDTO<List<DUser>> resultDTO = homeService.login(userBaseDTO);

        if (resultDTO.isSuccess() && resultDTO.getData().size() > 0) {
            return ResultDTO.success(resultDTO);
        } else {
            return ResultDTO.failure(ResultError.error("用户名或密码不正确！"));
        }

    }

}
