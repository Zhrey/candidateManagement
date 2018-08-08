package com.ray.core.api.service;

import com.ray.cloud.framework.base.dto.ResultDTO;

import java.io.File;

/**
 * Created by ZhangRui on 2018/8/5.
 */
public interface ResumeService {

    ResultDTO uploadFile(File file);

    ResultDTO downloadFile(String filePath, String fileName);
}
