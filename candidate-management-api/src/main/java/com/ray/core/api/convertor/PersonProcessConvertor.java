package com.ray.core.api.convertor;

import com.ray.cloud.framework.base.convertor.AbstractConvertor;
import com.ray.cloud.framework.mybatis.entity.DPersonBase;
import com.ray.cloud.framework.mybatis.entity.DPersonProcess;
import com.ray.core.sdk.dto.SearchTaskResultDTO;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 13:23 2018/8/16
 * @Modified By:
 */
public class PersonProcessConvertor extends AbstractConvertor<SearchTaskResultDTO,DPersonProcess> {

    public SearchTaskResultDTO toDTO(DPersonProcess dPersonProcess) {

        if (dPersonProcess != null) {

            SearchTaskResultDTO searchTaskResultDTO = new SearchTaskResultDTO();
            BeanUtils.copyProperties(dPersonProcess,searchTaskResultDTO);
            if (dPersonProcess.getSubmitDate() != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                searchTaskResultDTO.setSubmitDate(simpleDateFormat.format(dPersonProcess.getSubmitDate()));
            }

            return searchTaskResultDTO;
        }

        return null;
    }

    public void baseToDTO(SearchTaskResultDTO searchTaskResultDTO,DPersonBase dPersonBase) {

        if (dPersonBase != null) {

            searchTaskResultDTO.setName(dPersonBase.getName());
            searchTaskResultDTO.setMobile(dPersonBase.getMobile());
            searchTaskResultDTO.setMail(dPersonBase.getMail());
            searchTaskResultDTO.setSex(dPersonBase.getSex());
        }
    }

    public DPersonProcess toEntity(SearchTaskResultDTO dto) {
        return null;
    }
}
