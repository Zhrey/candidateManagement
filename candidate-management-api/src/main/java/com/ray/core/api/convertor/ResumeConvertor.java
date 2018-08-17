package com.ray.core.api.convertor;

import com.ray.cloud.framework.base.convertor.AbstractConvertor;
import com.ray.cloud.framework.mybatis.entity.DPersonBase;
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
    /**
     * @Author: ZhangRui
     * @param: dPersonBase
     * @Description: 将个人基本信息组装近返回值实体类
     * @date: Created in 16:04 2018/8/17
     */
    public void personInfoToDTO(SearchResumeResultDTO searchResumeResultDTO,DPersonBase dPersonBase) {

        if (dPersonBase != null) {
            searchResumeResultDTO.setName(dPersonBase.getName());
            searchResumeResultDTO.setSex(dPersonBase.getSex());
            searchResumeResultDTO.setMobile(dPersonBase.getMobile());
            searchResumeResultDTO.setWorkingTime(dPersonBase.getWorkingTime());
        }

    }

    public DResume toEntity(SearchResumeResultDTO dto) {
        return null;
    }
}
