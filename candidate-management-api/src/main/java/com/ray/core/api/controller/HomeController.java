package com.ray.core.api.controller;

import com.ray.cloud.framework.base.Enum.DeleteFlagEnum;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.base.dto.ResultError;
import com.ray.cloud.framework.mybatis.entity.DUcUser;
import com.ray.cloud.framework.mybatis.entity.DUcUserExample;
import com.ray.cloud.framework.mybatis.service.DUcUserService;
import com.ray.core.api.dto.UserBaseDTO;
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
    private DUcUserService dUcUserService;

    @RequestMapping("login")
    public ResultDTO login(@RequestBody UserBaseDTO userBaseDTO) {
        DUcUserExample dUcUserExample = new DUcUserExample();
        dUcUserExample.createCriteria().andUserNameEqualTo(userBaseDTO.getUsername())
                .andPasswordEqualTo(userBaseDTO.getPassword())
                .andDataFlagEqualTo(DeleteFlagEnum.NON_DELETE.ordinal());
        ResultDTO<List<DUcUser>> resultDTO = dUcUserService.selectByExample(dUcUserExample);
        if (resultDTO.isSuccess() && resultDTO.getData().size() > 0) {
            return ResultDTO.success(resultDTO);
        } else {
            return ResultDTO.failure(ResultError.error("用户名或密码不正确！"));
        }
    }
}
