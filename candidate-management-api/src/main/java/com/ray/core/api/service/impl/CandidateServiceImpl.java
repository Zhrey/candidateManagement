package com.ray.core.api.service.impl;

import com.ray.cloud.framework.base.dto.PageResultDTO;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.base.dto.ResultError;
import com.ray.cloud.framework.mybatis.entity.DPersonBase;
import com.ray.cloud.framework.mybatis.entity.DPersonBaseExample;
import com.ray.cloud.framework.mybatis.service.DPersonBaseService;
import com.ray.core.api.convertor.PersonBaseConvertor;
import com.ray.core.api.service.CandidateService;
import com.ray.core.sdk.dto.SearchCandidateDTO;
import com.ray.core.sdk.dto.SearchCandidateResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@Service
@Slf4j
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private DPersonBaseService dPersonBaseService;

    /**
     * @Author: ZhangRui
     * @param: searchCandidateDTO
     * @Description: 查询候选人基本信息
     * @date: Created in 11:33 2018/8/16
     */
    public ResultDTO<PageResultDTO<SearchCandidateResultDTO>> searchCandidate(SearchCandidateDTO searchCandidateDTO) {

        DPersonBaseExample dPersonBaseExample = new DPersonBaseExample();
        DPersonBaseExample.Criteria criteria = dPersonBaseExample.createCriteria();
        if (StringUtils.isNotEmpty(searchCandidateDTO.getName())) {
            criteria.andNameEqualTo(searchCandidateDTO.getName());
        }
        criteria.andDataFlagEqualTo(0);

        ResultDTO<PageResultDTO<DPersonBase>> resultDTO = dPersonBaseService
                .selectByExamplePageable(dPersonBaseExample,searchCandidateDTO.getPageNo(),searchCandidateDTO.getPageSize());

        if (resultDTO.isSuccess()) {
            PersonBaseConvertor personBaseConvertor = new PersonBaseConvertor();

            return personBaseConvertor.toPageResultDTO(resultDTO.getData());
        } else {
            return ResultDTO.failure(ResultError.error("请联系管理员，获取候选人信息异常！"));
        }
    }
}
