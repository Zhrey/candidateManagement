package com.ray.core.api.dto;

import com.ray.cloud.framework.base.dto.AbstractDTO;
import lombok.Data;

import java.util.Date;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 17:50 2018/8/10
 * @Modified By:
 */
@Data
public class PersonBaseDTO extends AbstractDTO{

    //姓名
    private String name;
    //性别
    private String sex;
    //出生日期
    private Date birthday;
    //工作年限
    private String workingTime;
    //移动电话
    private String mobile;
    //电子邮件
    private String mail;
    //目前居住地
    private String currentAddress;
    //通讯地址
    private String postalAddress;
    //户口
    private String hukou;
    //专业名称
    private String major;
    //学校名称
    private String school;
    //最高学历
    private String maxEducation;
}
