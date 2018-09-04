package com.ray.core.api.service;

import com.ray.cloud.framework.base.dto.PageResultDTO;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.core.sdk.dto.SearchSituationDTO;
import com.ray.core.sdk.dto.SearchSituationResultDTO;
import com.ray.core.sdk.dto.SearchTaskDTO;
import com.ray.core.sdk.dto.SearchTaskResultDTO;

/**
 * Created by ZhangRui on 2018/8/5.
 */
public interface TaskService {

    /**
     * @Author: ZhangRui
     * @param: searchTaskDTO
     * @Description: 查询候选人沟通信息
     * @date: Created in 11:33 2018/8/16
     */
    ResultDTO<PageResultDTO<SearchTaskResultDTO>> searchTask(SearchTaskDTO searchTaskDTO);

    /**
     * @Author: ZhangRui
     * @param: searchSituationDTO
     * @Description: 查询候选人沟通信息列表
     * @date: Created in 11:33 2018/8/16
     */
    ResultDTO<PageResultDTO<SearchSituationResultDTO>> searchCandidate(SearchSituationDTO searchSituationDTO);

}
