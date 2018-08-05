package com.ray.core.sdk.dto;

import com.ray.cloud.framework.base.dto.AbstractDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@Data
public class FileBaseDTO extends AbstractDTO {

    //附件名称
    private String fileName;
    //附件地址
    private String filePath;
    //附件类型
    private Integer fileType;

}
