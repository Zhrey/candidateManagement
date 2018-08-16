package com.ray.core.api.service;

import com.ray.cloud.framework.base.dto.PageResultDTO;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.core.sdk.dto.SearchCandidateDTO;
import com.ray.core.sdk.dto.SearchCandidateResultDTO;

/**
 * Created by ZhangRui on 2018/8/5.
 */
public interface CandidateService {

    /**
     * @Author: ZhangRui
     * @param: multipartFile
     * @Description: 查询候选人基本信息
     * @date: Created in 11:33 2018/8/16
     */
    ResultDTO<PageResultDTO<SearchCandidateResultDTO>> searchCandidate(SearchCandidateDTO searchCandidateDTO);

}
