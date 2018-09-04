package com.ray.core.api.convertor;

import com.ray.cloud.framework.base.convertor.AbstractConvertor;
import com.ray.cloud.framework.mybatis.entity.DPersonSituation;
import com.ray.core.api.enums.StatusEnum;
import com.ray.core.sdk.dto.SearchSituationResultDTO;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 13:23 2018/8/16
 * @Modified By:
 */
public class PersonSituationConvertor extends AbstractConvertor<SearchSituationResultDTO,DPersonSituation> {

    public SearchSituationResultDTO toDTO(DPersonSituation dPersonSituation) {

        if (dPersonSituation != null) {

            SearchSituationResultDTO searchSituationResultDTO = new SearchSituationResultDTO();
            BeanUtils.copyProperties(dPersonSituation,searchSituationResultDTO);
            if (dPersonSituation.getTalkDate() != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                searchSituationResultDTO.setTalkDate(simpleDateFormat.format(dPersonSituation.getTalkDate()));
            }
            if (dPersonSituation.getSituationType() != null) {
                if (dPersonSituation.getSituationType().equals(StatusEnum.CANTACTING.getValue())) {
                    searchSituationResultDTO.setSituationType(StatusEnum.CANTACTING.getName());
                }else if (dPersonSituation.getSituationType().equals(StatusEnum.FIRST_INTERVIEW.getValue())) {
                    searchSituationResultDTO.setSituationType(StatusEnum.FIRST_INTERVIEW.getName());
                }else if (dPersonSituation.getSituationType().equals(StatusEnum.SECOND_INTERVIEW.getValue())) {
                    searchSituationResultDTO.setSituationType(StatusEnum.SECOND_INTERVIEW.getName());
                }
            }

            return searchSituationResultDTO;
        }

        return null;
    }

    public DPersonSituation toEntity(SearchSituationResultDTO dto) {
        return null;
    }
}
