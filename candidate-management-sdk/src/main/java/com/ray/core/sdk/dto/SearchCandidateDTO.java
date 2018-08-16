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
public class SearchCandidateDTO extends AbstractDTO{

    //姓名
    private String name;
    //性别
    private String sex;
    //工作年限
    private String workingTime;
    //最高学历
    private String maxEducation;
    //页码
    private int pageNo;
    //每页条数
    private int pageSize;
}
