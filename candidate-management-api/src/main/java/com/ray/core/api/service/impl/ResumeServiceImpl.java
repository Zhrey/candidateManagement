package com.ray.core.api.service.impl;

import com.ray.cloud.framework.base.dto.PageResultDTO;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.base.dto.ResultError;
import com.ray.cloud.framework.file.sdk.service.FileSdkService;
import com.ray.cloud.framework.mybatis.entity.DPersonBase;
import com.ray.cloud.framework.mybatis.entity.DPersonBaseExample;
import com.ray.cloud.framework.mybatis.entity.DResume;
import com.ray.cloud.framework.mybatis.entity.DResumeExample;
import com.ray.cloud.framework.mybatis.service.DPersonBaseService;
import com.ray.cloud.framework.mybatis.service.DResumeService;
import com.ray.core.api.convertor.ResumeConvertor;
import com.ray.core.api.enums.PersonBaseEnum;
import com.ray.core.api.enums.ResumeEnum;
import com.ray.core.api.service.ResumeService;
import com.ray.core.api.utils.ImportExecl;
import com.ray.core.api.utils.WDWUtil;
import com.ray.core.sdk.dto.SearchResumeDTO;
import com.ray.core.sdk.dto.SearchResumeResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@Service
@Slf4j
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private FileSdkService fileSdkService;

    @Autowired
    private DPersonBaseService dPersonBaseService;

    @Autowired
    private DResumeService dResumeService;

    public ResultDTO downloadFile(String filePath, String fileName){

        return fileSdkService.downloadFile(filePath,fileName);
    }


    /**
     * @Author: ZhangRui
     * @param: searchResumeDTO
     * @Description: 查询简历基本信息
     * @date: Created in 11:33 2018/8/16
     */
    public ResultDTO<PageResultDTO<SearchResumeResultDTO>> searchResume(SearchResumeDTO searchResumeDTO) {

        DResumeExample dResumeExample = new DResumeExample();
        DResumeExample.Criteria criteria = dResumeExample.createCriteria();
        criteria.andDataFlagEqualTo(0);

        ResultDTO<PageResultDTO<DResume>> resultDTO = dResumeService
                .selectByExamplePageable(dResumeExample,searchResumeDTO.getPageNo(),searchResumeDTO.getPageSize());

        if (resultDTO.isSuccess()) {
            ResumeConvertor resumeConvertor = new ResumeConvertor();

            return resumeConvertor.toPageResultDTO(resultDTO.getData());
        } else {
            return ResultDTO.failure(ResultError.error("请联系管理员，获取简历信息异常！"));
        }
    }

    /**
     * @Author: ZhangRui
     * @param: multipartFile
     * @Description: 上传简历
     * @date: Created in 11:33 2018/8/16
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO uploadFile(File file) {


        if (!file.exists()) {
            return ResultDTO.failure(ResultError.error("请联系管理员，目标文件不存在！"));
        }
        ImportExecl poi = new ImportExecl();
        Map<String,List<List<String>>> map = poi.read(file, file.getName());

        if (map.size() == 0) {
            log.error("读取excel时没有读取到数据！");
            return ResultDTO.failure(ResultError.error("简历模板异常，没有获取到相应信息！"));
        }
        //将简历sheet1中数据集合转换为键值对形式
        Map<String, Object> baseMap = listToMap(map.get("sheet1"));

        //读取excel1转换dto
        ResultDTO baseRes = readExcelToBaseInfo(baseMap);
        if (baseRes.isSuccess()) {
            //保存简历基本信息
            ResultDTO resumeResult = readExcelToResumeInfo(baseMap,(String) baseRes.getData());

            if (!resumeResult.isSuccess()) {
                //保存简历基本信息出错
                return resumeResult;
            }
        }else{
            //保存人员基本信息出错
            return baseRes;
        }

//        ResultDTO resultDTO = fileSdkService.uploadFile(file);
        return ResultDTO.success();
    }

    /**
     * @Author: ZhangRui
     * @param: list 简历sheet1中数据
     * @Description:   将简历sheet1中数据集合转换为键值对形式
     * @date: Created in 17:05 2018/8/14
     */
    private Map<String, Object> listToMap(List<List<String>> list) {

        Map<String, Object> map = new HashMap<String, Object>();

        List<String> keyList = list.get(0);
        List<String> valList = list.get(1);

        for (int i =0;i<keyList.size();i++) {
            //人员基本信息
            String key = PersonBaseEnum.getValue(keyList.get(i));
            if (StringUtils.isNotEmpty(key)) {
                map.put(key, valList.get(i));
            }
            //简历基本信息
            String resumekey = ResumeEnum.getValue(keyList.get(i));
            if (StringUtils.isNotEmpty(resumekey)) {
                map.put(resumekey, valList.get(i));
            }
        }

        return map;
    }


    /**
     * 读取Excel候选人基本信息
     *
     * @param param
     * @return
     */
    private ResultDTO readExcelToBaseInfo(Map<String, Object> param) {

        DPersonBase dPersonBase = new DPersonBase();

        //转换
        ResultDTO result = mapToEntity(dPersonBase, param);
        if (!result.isSuccess()) {
            return result;
        }
        DPersonBaseExample dPersonBaseExample = new DPersonBaseExample();
        //验证该候选人是否已经导入过
        dPersonBaseExample.createCriteria()
                .andNameEqualTo(dPersonBase.getName())
                .andMobileEqualTo(dPersonBase.getMobile())
                .andDataFlagEqualTo(0);
        ResultDTO<List<DPersonBase>> listResultDTO = dPersonBaseService.selectByExample(dPersonBaseExample);

        if (listResultDTO.isSuccess() && listResultDTO.getData() != null && listResultDTO.getData().size() > 0) {
            return ResultDTO.failure(ResultError.error("上传错误，该候选人已经上传过简历！"));
        } else {
            ResultDTO<String> resultDTO = dPersonBaseService.insertSelective(dPersonBase);
            if (resultDTO.isSuccess()) {
                return resultDTO;
            }else {
                return ResultDTO.failure(ResultError.error("请联系管理员，简历信息入库失败！"));
            }
        }

    }
    /**
     * 读取Excel简历基本信息
     *
     * @param param
     * @return
     */
    private ResultDTO readExcelToResumeInfo(Map<String, Object> param,String personId) {

        DResume dResume = new DResume();
        //转换
        ResultDTO result = mapToEntity(dResume, param);
        if (!result.isSuccess()) {
            return result;
        }
        dResume.setPersonId(personId);
        ResultDTO<String> resultDTO = dResumeService.insertSelective(dResume);
        if (resultDTO.isSuccess()) {
            return resultDTO;
        }else {

            return ResultDTO.failure(ResultError.error("请联系管理员，简历信息入库失败！"));
        }

    }


    private<T> ResultDTO mapToEntity(T t,Map<String, Object> param) {

        if (param != null) {
            Field[] fields = t.getClass().getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);
                try {
                    if (StringUtils.isEmpty((String) param.get(field.getName()))) {
                        continue;
                    }
                    if (field.getType() == Integer.class) {
                    } else if (field.getType() == BigDecimal.class) {
                        field.set(t, new BigDecimal((String) param.get(field.getName())));
                    } else if (field.getType() == Date.class) {
                        SimpleDateFormat simpleDateFormat;
                        if (((String) param.get(field.getName())).length() > 9) {
                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        } else {
                            simpleDateFormat = new SimpleDateFormat("yyyy/MM");
                        }
                        Date birthday = simpleDateFormat.parse((String) param.get(field.getName()));
                        field.set(t, birthday);
                    } else {
                        log.info(field.getName());
                        log.info(WDWUtil.ran2fuc((String) param.get(field.getName())));
                        field.set(t, WDWUtil.ran2fuc((String) param.get(field.getName())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("赋值错误：" + e.getMessage(), e);
                    return ResultDTO.failure(ResultError.error("赋值错误,简历模板异常！"));
                }
            }
            return ResultDTO.success();
        } else {
            log.error("companyInfoStringList：null");
            return ResultDTO.failure(ResultError.error("简历模板异常，没有获取到相应信息！"));
        }
    }
}
