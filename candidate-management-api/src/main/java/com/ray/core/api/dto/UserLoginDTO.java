package com.ray.core.api.dto;

import com.ray.cloud.framework.base.dto.AbstractDTO;
import lombok.Data;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 14:35 2018/7/31
 * @Modified By:
 */
@Data
public class UserLoginDTO extends AbstractDTO{

    //用户名
    private String username;
    // 密码
    private String password;
}
