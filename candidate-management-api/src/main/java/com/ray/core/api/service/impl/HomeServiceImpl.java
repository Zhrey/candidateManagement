package com.ray.core.api.service.impl;

import com.ray.cloud.framework.base.Enum.DataFlagEnum;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.mybatis.entity.DUser;
import com.ray.cloud.framework.mybatis.entity.DUserExample;
import com.ray.cloud.framework.mybatis.service.DUserService;
import com.ray.core.api.dto.UserLoginDTO;
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
    private DUserService dUserService;

    public ResultDTO<List<DUser>> login(UserLoginDTO userLoginDTO) {

        DUserExample dUcUserExample = new DUserExample();
        dUcUserExample.createCriteria().andUserNameEqualTo(userLoginDTO.getUsername())
                .andPasswordEqualTo(userLoginDTO.getPassword())
                .andDataFlagEqualTo(DataFlagEnum.NON_DELETE.ordinal());
        ResultDTO<List<DUser>> resultDTO = dUserService.selectByExample(dUcUserExample);

        return resultDTO;
    }
}
