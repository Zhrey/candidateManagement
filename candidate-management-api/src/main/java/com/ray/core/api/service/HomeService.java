package com.ray.core.api.service;

import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.mybatis.entity.DUser;
import com.ray.core.api.dto.UserBaseDTO;

import java.util.List;

/**
 * Created by ZhangRui on 2018/8/5.
 */
public interface HomeService {

    ResultDTO<List<DUser>> login(UserBaseDTO userBaseDTO);
}
