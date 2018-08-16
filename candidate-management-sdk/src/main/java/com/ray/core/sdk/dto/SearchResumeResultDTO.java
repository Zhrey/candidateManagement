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
public class SearchResumeResultDTO extends AbstractDTO{

    /************************人员基本信息*************************/
    //姓名
    private String name;
    //性别
    private String sex;
    //工作年限
    private String workingTime;
    //移动电话
    private String mobile;
    /************************简历基本信息*************************/
    //简历名称
    private String resumeName;
    //标签名称
    private String tagName;
    //应聘职位
    private String jobPosition;
    //现在单位
    private String currentCompany;
    //期望月薪(税前)
    private String expectSalary;
    //投递(收藏)时间
    private String deliveryDate;
}
