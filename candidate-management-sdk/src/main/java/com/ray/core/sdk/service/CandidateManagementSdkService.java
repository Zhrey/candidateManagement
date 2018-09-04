package com.ray.core.sdk.service;

import com.ray.cloud.framework.base.dto.PageResultDTO;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.core.sdk.dto.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 15:25 2018/7/26
 * @Modified By:
 */
@FeignClient(value = "candidate-management")
public interface CandidateManagementSdkService {

    @RequestMapping("login")
    ResultDTO login(UserBaseDTO userBaseDTO);

    /**
     * @Author: ZhangRui
     * @param: searchCandidateDTO
     * @Description: 查询候选人基本信息
     * @date: Created in 11:33 2018/8/16
     */
    @RequestMapping(value = "resume/searchCandidate")
    ResultDTO<PageResultDTO<SearchCandidateResultDTO>> searchCandidate(SearchCandidateDTO searchCandidateDTO);

    /**
     * @Author: ZhangRui
     * @param: searchTaskDTO
     * @Description: 查询候选人沟通信息
     * @date: Created in 11:33 2018/8/16
     */
    @RequestMapping(value = "task/searchTask")
    ResultDTO<PageResultDTO<SearchTaskResultDTO>> searchTask(SearchTaskDTO searchTaskDTO);

    /**
     * @Author: ZhangRui
     * @param: searchSituationDTO
     * @Description: 查询候选人沟通信息列表
     * @date: Created in 11:33 2018/8/16
     */
    @RequestMapping(value = "task/searchSituation")
    ResultDTO<PageResultDTO<SearchSituationResultDTO>> searchSituation(SearchSituationDTO searchSituationDTO);

    /**
     * @Author: ZhangRui
     * @param: searchResumeDTO
     * @Description: 查询简历基本信息
     * @date: Created in 11:33 2018/8/16
     */
    @RequestMapping(value = "resume/searchResume")
    ResultDTO<PageResultDTO<SearchResumeResultDTO>> searchResume(SearchResumeDTO searchResumeDTO);

    /**
     * @Author: ZhangRui
     * @param: multipartFile
     * @Description: 上传简历
     * @date: Created in 11:33 2018/8/16
     */
    @RequestMapping(value = "resume/uploadFile")
    ResultDTO uploadFile(@RequestParam("fileModel") File file,@RequestParam("fileName") String fileName);

}
