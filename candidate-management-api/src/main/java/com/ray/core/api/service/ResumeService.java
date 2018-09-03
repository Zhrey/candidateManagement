package com.ray.core.api.service;

import com.ray.cloud.framework.base.dto.PageResultDTO;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.core.sdk.dto.SearchResumeDTO;
import com.ray.core.sdk.dto.SearchResumeResultDTO;

import java.io.File;

/**
 * Created by ZhangRui on 2018/8/5.
 */
public interface ResumeService {

    ResultDTO uploadFile(File file,String fileName);

    /**
     * @Author: ZhangRui
     * @param: searchResumeDTO
     * @Description: 查询简历基本信息
     * @date: Created in 11:33 2018/8/16
     */
    ResultDTO<PageResultDTO<SearchResumeResultDTO>> searchResume(SearchResumeDTO searchResumeDTO);
}
