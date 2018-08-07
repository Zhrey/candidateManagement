package com.ray.core.api.service.impl;

import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.file.sdk.service.FileSdkService;
import com.ray.core.api.service.ResumeService;
import com.ray.core.api.utils.CountFileUtil;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private FileSdkService fileSdkService;

    @Transactional(rollbackFor = Exception.class)
    public ResultDTO uploadFile(MultipartFile multipartFile) {

        File file = CountFileUtil.gettempfile(multipartFile);
        try {
            HWPFDocument doc = new HWPFDocument(new FileInputStream(file));

            //通过 Doc对象直接获取Text
            StringBuilder sb = doc.getText();

            //通过Range对象获取Text
            Range range = doc.getRange();
            String text = range.text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResultDTO resultDTO = fileSdkService.uploadFile(multipartFile);
        return resultDTO;
    }

    public ResultDTO downloadFile(String filePath, String fileName){

        return fileSdkService.downloadFile(filePath,fileName);
    }
}
