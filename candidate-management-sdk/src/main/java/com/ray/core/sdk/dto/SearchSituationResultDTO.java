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
public class SearchSituationResultDTO extends AbstractDTO{

    //沟通类型
    private String situationType;
    //面试结果
    private Integer interviewResult;
    //描述
    private String memo;
    //沟通时间
    private String talkDate;
}
