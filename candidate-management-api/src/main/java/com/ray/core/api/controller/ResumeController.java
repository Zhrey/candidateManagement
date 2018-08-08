package com.ray.core.api.controller;

import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.core.api.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@RestController
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @RequestMapping("uploadFile")
    public ResultDTO uploadFile(@RequestParam("fileModel") File file) {

        return resumeService.uploadFile(file);
    }

    @RequestMapping("downloadFile")
    public ResultDTO downloadFile(@RequestParam String filePath, String fileName) {

        return resumeService.downloadFile(filePath,fileName);
    }
}
