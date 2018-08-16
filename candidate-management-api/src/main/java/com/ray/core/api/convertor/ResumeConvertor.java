package com.ray.core.api.convertor;

import com.ray.cloud.framework.base.convertor.AbstractConvertor;
import com.ray.cloud.framework.mybatis.entity.DResume;
import com.ray.core.sdk.dto.SearchResumeResultDTO;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 13:23 2018/8/16
 * @Modified By:
 */
public class ResumeConvertor extends AbstractConvertor<SearchResumeResultDTO,DResume> {

    public SearchResumeResultDTO toDTO(DResume dResume) {

        if (dResume != null) {

            SearchResumeResultDTO searchResumeResultDTO = new SearchResumeResultDTO();
            BeanUtils.copyProperties(dResume,searchResumeResultDTO);
            if (dResume.getDeliveryDate() != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                searchResumeResultDTO.setDeliveryDate(simpleDateFormat.format(dResume.getDeliveryDate()));
            }

            return searchResumeResultDTO;
        }

        return null;
    }

    public DResume toEntity(SearchResumeResultDTO dto) {
        return null;
    }
}
