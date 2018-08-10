package com.ray.core.api.service.impl;

import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.file.sdk.service.FileSdkService;
import com.ray.core.api.service.ResumeService;
import com.ray.core.api.utils.ImportExecl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@Service
@Slf4j
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private FileSdkService fileSdkService;

    public ResultDTO downloadFile(String filePath, String fileName){

        return fileSdkService.downloadFile(filePath,fileName);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultDTO uploadFile(File file) {

        try {

            if (!file.exists()) {
                throw new RuntimeException("目标文件不存在！");
            }
            ImportExecl poi = new ImportExecl();
            Map<String,List<List<String>>> map = new HashMap<String,List<List<String>>>();
            map = poi.read(file, file.getName());
            //读取excel转换dto
            poi.readModelToCompany(map.get("sheet1"));


        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
//        ResultDTO resultDTO = fileSdkService.uploadFile(file);
        return ResultDTO.success();
    }
}
