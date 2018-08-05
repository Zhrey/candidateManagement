package com.ray.core.api.service.impl;

import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.file.sdk.service.FileSdkService;
import com.ray.core.api.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private FileSdkService fileSdkService;

    public ResultDTO uploadFile(MultipartFile multipartFile) {

        ResultDTO resultDTO = fileSdkService.uploadFile(multipartFile);
        return resultDTO;
    }

    public ResultDTO downloadFile(String filePath, String fileName) {

        return fileSdkService.downloadFile(filePath,fileName);
    }
}
