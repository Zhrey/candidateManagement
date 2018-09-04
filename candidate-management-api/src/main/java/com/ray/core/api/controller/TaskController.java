package com.ray.core.api.controller;

import com.ray.cloud.framework.base.dto.PageResultDTO;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.core.api.service.TaskService;
import com.ray.core.sdk.dto.SearchSituationDTO;
import com.ray.core.sdk.dto.SearchSituationResultDTO;
import com.ray.core.sdk.dto.SearchTaskDTO;
import com.ray.core.sdk.dto.SearchTaskResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@RestController
@RequestMapping(value = "task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * @Author: ZhangRui
     * @param: searchTaskDTO
     * @Description: 查询候选人沟通信息
     * @date: Created in 11:33 2018/8/16
     */
    @RequestMapping(value = "searchTask", method = RequestMethod.POST)
    public ResultDTO<PageResultDTO<SearchTaskResultDTO>> searchCandidate(@RequestBody SearchTaskDTO searchTaskDTO) {

        ResultDTO<PageResultDTO<SearchTaskResultDTO>> resultDTO = taskService.searchTask(searchTaskDTO);
        return resultDTO;
    }
    /**
     * @Author: ZhangRui
     * @param: searchSituationDTO
     * @Description: 查询候选人沟通信息列表
     * @date: Created in 11:33 2018/8/16
     */
    @RequestMapping(value = "searchSituation", method = RequestMethod.POST)
    public ResultDTO<PageResultDTO<SearchSituationResultDTO>> searchSituation(@RequestBody SearchSituationDTO searchSituationDTO) {

        ResultDTO<PageResultDTO<SearchSituationResultDTO>> resultDTO = taskService.searchCandidate(searchSituationDTO);
        return resultDTO;
    }
}
