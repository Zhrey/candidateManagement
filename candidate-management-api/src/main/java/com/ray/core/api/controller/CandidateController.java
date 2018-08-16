package com.ray.core.api.controller;

import com.ray.cloud.framework.base.dto.PageResultDTO;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.core.api.service.CandidateService;
import com.ray.core.sdk.dto.SearchCandidateDTO;
import com.ray.core.sdk.dto.SearchCandidateResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@RestController
@RequestMapping(value = "resume")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    /**
     * @Author: ZhangRui
     * @param: multipartFile
     * @Description: 查询候选人基本信息
     * @date: Created in 11:33 2018/8/16
     */
    @RequestMapping(value = "searchCandidate", method = RequestMethod.POST)
    public ResultDTO<PageResultDTO<SearchCandidateResultDTO>> searchCandidate(@RequestBody SearchCandidateDTO searchCandidateDTO) {

        ResultDTO<PageResultDTO<SearchCandidateResultDTO>> resultDTO = candidateService.searchCandidate(searchCandidateDTO);
        return resultDTO;
    }
}
