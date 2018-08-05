package com.ray.core.api.service.impl;

import com.ray.cloud.framework.base.Enum.DeleteFlagEnum;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.mybatis.entity.DUcUser;
import com.ray.cloud.framework.mybatis.entity.DUcUserExample;
import com.ray.cloud.framework.mybatis.service.DUcUserService;
import com.ray.core.api.dto.UserBaseDTO;
import com.ray.core.api.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@Service
public class HomeServiceImpl implements HomeService{

    @Autowired
    private DUcUserService dUcUserService;

    public ResultDTO<List<DUcUser>> login(UserBaseDTO userBaseDTO) {

        DUcUserExample dUcUserExample = new DUcUserExample();
        dUcUserExample.createCriteria().andUserNameEqualTo(userBaseDTO.getUsername())
                .andPasswordEqualTo(userBaseDTO.getPassword())
                .andDataFlagEqualTo(DeleteFlagEnum.NON_DELETE.ordinal());
        ResultDTO<List<DUcUser>> resultDTO = dUcUserService.selectByExample(dUcUserExample);

        return resultDTO;
    }
}
