package com.ray.core.sdk.dto;

import com.ray.cloud.framework.base.dto.AbstractDTO;
import lombok.Data;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 13:13 2018/8/16
 * @Modified By:
 */
@Data
public class SearchTaskResultDTO extends AbstractDTO{

    private String personId;
    //姓名
    private String name;
    //推荐职位
    private String recommendPosition;
    //移动电话
    private String mobile;
    //性别
    private String sex;
    //电子邮件
    private String mail;
    //提交简历日期
    private String submitDate;
    //简历来源
    private String comeFrom;
    //状态
    private Integer status;
}
