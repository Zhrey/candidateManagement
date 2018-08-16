package com.ray.core.api.convertor;

import com.ray.cloud.framework.base.convertor.AbstractConvertor;
import com.ray.cloud.framework.mybatis.entity.DPersonBase;
import com.ray.core.sdk.dto.SearchCandidateResultDTO;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 13:23 2018/8/16
 * @Modified By:
 */
public class PersonBaseConvertor extends AbstractConvertor<SearchCandidateResultDTO,DPersonBase> {

    public SearchCandidateResultDTO toDTO(DPersonBase dPersonBase) {

        if (dPersonBase != null) {

            SearchCandidateResultDTO searchCandidateResultDTO = new SearchCandidateResultDTO();
            BeanUtils.copyProperties(dPersonBase,searchCandidateResultDTO);
            if (dPersonBase.getBirthday() != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                searchCandidateResultDTO.setBirthday(simpleDateFormat.format(dPersonBase.getBirthday()));
            }

            return searchCandidateResultDTO;
        }

        return null;
    }

    public DPersonBase toEntity(SearchCandidateResultDTO dto) {
        return null;
    }
}
