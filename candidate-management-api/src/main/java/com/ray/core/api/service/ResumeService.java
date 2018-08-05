package com.ray.core.api.service;

import com.ray.cloud.framework.base.dto.ResultDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by ZhangRui on 2018/8/5.
 */
public interface ResumeService {

    ResultDTO uploadFile(MultipartFile multipartFile);

    ResultDTO downloadFile(String filePath, String fileName);
}
