package com.ray.core.api.controller;

import com.ray.cloud.framework.base.dto.PageResultDTO;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.core.api.service.ResumeService;
import com.ray.core.sdk.dto.SearchResumeDTO;
import com.ray.core.sdk.dto.SearchResumeResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@Slf4j
@RestController
@RequestMapping(value = "resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    /**
     * @Author: ZhangRui
     * @param: multipartFile
     * @Description: 上传简历
     * @date: Created in 11:33 2018/8/16
     */
    @RequestMapping("uploadFile")
    public ResultDTO uploadFile(@RequestParam("fileModel") File file) {

        return resumeService.uploadFile(file);
    }

    @RequestMapping("downloadFile")
    public ResultDTO downloadFile(@RequestParam String filePath, String fileName) {

        return resumeService.downloadFile(filePath,fileName);
    }


    /**
     * @Author: ZhangRui
     * @param: searchCandidateDTO
     * @Description: 查询简历基本信息
     * @date: Created in 11:33 2018/8/16
     */
    @RequestMapping(value = "searchResume", method = RequestMethod.POST)
    public ResultDTO<PageResultDTO<SearchResumeResultDTO>> searchResume(@RequestBody SearchResumeDTO searchResumeDTO) {

        return resumeService.searchResume(searchResumeDTO);
    }
}
