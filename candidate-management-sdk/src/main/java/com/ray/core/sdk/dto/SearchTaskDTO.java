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
public class SearchTaskDTO extends AbstractDTO{

    //姓名
    private String name;
    //页码
    private int pageNo;
    //每页条数
    private int pageSize;
}
