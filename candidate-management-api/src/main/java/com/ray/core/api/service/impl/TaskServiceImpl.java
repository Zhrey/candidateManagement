package com.ray.core.api.service.impl;

import com.ray.cloud.framework.base.dto.PageResultDTO;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.base.dto.ResultError;
import com.ray.cloud.framework.mybatis.entity.DPersonBase;
import com.ray.cloud.framework.mybatis.entity.DPersonProcess;
import com.ray.cloud.framework.mybatis.entity.DPersonProcessExample;
import com.ray.cloud.framework.mybatis.service.DPersonBaseService;
import com.ray.cloud.framework.mybatis.service.DPersonProcessService;
import com.ray.core.api.convertor.PersonProcessConvertor;
import com.ray.core.api.service.TaskService;
import com.ray.core.sdk.dto.SearchTaskDTO;
import com.ray.core.sdk.dto.SearchTaskResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    private DPersonProcessService dPersonProcessService;
    @Autowired
    private DPersonBaseService dPersonBaseService;

    /**
     * @Author: ZhangRui
     * @param: searchTaskDTO
     * @Description: 查询候选人沟通信息
     * @date: Created in 11:33 2018/8/16
     */
    public ResultDTO<PageResultDTO<SearchTaskResultDTO>> searchTask(SearchTaskDTO searchTaskDTO) {


        DPersonProcessExample dPersonProcessExample = new DPersonProcessExample();
        DPersonProcessExample.Criteria criteria = dPersonProcessExample.createCriteria();
        criteria.andDataFlagEqualTo(0);
        dPersonProcessExample.setOrderByClause("CREATE_TIME DESC");

        ResultDTO<PageResultDTO<DPersonProcess>> resultDTO = dPersonProcessService
                .selectByExamplePageable(dPersonProcessExample,searchTaskDTO.getPageNo(),searchTaskDTO.getPageSize());

        if (resultDTO.isSuccess()) {

            PersonProcessConvertor personProcessConvertor = new PersonProcessConvertor();
            ResultDTO<PageResultDTO<SearchTaskResultDTO>> result =  personProcessConvertor.toPageResultDTO(resultDTO.getData());
            //基本信息
            if (result.getData().getRows() != null) {
                for (SearchTaskResultDTO searchTaskResultDTO : result.getData().getRows()) {

                    ResultDTO<DPersonBase> baseResultDTO = dPersonBaseService.selectByPrimaryKey(searchTaskResultDTO.getPersonId());
                    if (baseResultDTO.isSuccess()) {
                        personProcessConvertor.baseToDTO(searchTaskResultDTO,baseResultDTO.getData());
                    }
                }
            }


            return result;
        } else {
            return ResultDTO.failure(ResultError.error("请联系管理员，获取候选人沟通信息异常！"));
        }
    }
}
