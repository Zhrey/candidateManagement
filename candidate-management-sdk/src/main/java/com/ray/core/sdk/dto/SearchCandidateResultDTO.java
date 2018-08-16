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
public class SearchCandidateResultDTO extends AbstractDTO{

    //姓名
    private String name;
    //性别
    private String sex;
    //出生日期
    private String birthday;
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
