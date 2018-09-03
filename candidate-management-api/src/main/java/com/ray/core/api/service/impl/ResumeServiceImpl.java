package com.ray.core.api.service.impl;

import com.ray.cloud.framework.base.dto.PageResultDTO;
import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.base.dto.ResultError;
import com.ray.cloud.framework.file.sdk.service.FileSdkService;
import com.ray.cloud.framework.mybatis.entity.*;
import com.ray.cloud.framework.mybatis.service.*;
import com.ray.core.api.convertor.ResumeConvertor;
import com.ray.core.api.enums.*;
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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private DJobWantService dJobWantService;
    @Autowired
    private DCardsService dCardsService;
    @Autowired
    private DWorkExperienceService dWorkExperienceService;
    @Autowired
    private DProjectExperienceService dProjectExperienceService;
    @Autowired
    private DTrainExperienceService dTrainExperienceService;
    @Autowired
    private DPersonOtherService dPersonOtherService;
    @Autowired
    private DPersonFileService dPersonFileService;

    /**
     * @Author: ZhangRui
     * @param: searchResumeDTO
     * @Description: 查询简历基本信息
     * @date: Created in 11:33 2018/8/16
     */
    public ResultDTO<PageResultDTO<SearchResumeResultDTO>> searchResume(SearchResumeDTO searchResumeDTO) {


        DPersonBaseExample dPersonBaseExample = new DPersonBaseExample();
        DPersonBaseExample.Criteria criteria = dPersonBaseExample.createCriteria();
        criteria.andDataFlagEqualTo(0);
        dPersonBaseExample.setOrderByClause("CREATE_TIME DESC");

        ResultDTO<PageResultDTO<DPersonBase>> resultDTO = dPersonBaseService
                .selectByExamplePageable(dPersonBaseExample,searchResumeDTO.getPageNo(),searchResumeDTO.getPageSize());

        if (resultDTO.isSuccess() && resultDTO.getData().getRows() != null) {

            ResumeConvertor resumeConvertor = new ResumeConvertor();
            List<SearchResumeResultDTO> list = new ArrayList<>();
            //循环人员匹配合同信息
            for(DPersonBase dPersonBase : resultDTO.getData().getRows()){
                SearchResumeResultDTO searchResumeResultDTO = null;
                DResumeExample dResumeExample = new DResumeExample();
                dResumeExample.createCriteria().andDataFlagEqualTo(0)
                        .andPersonIdEqualTo(dPersonBase.getId());
                ResultDTO<List<DResume>> resumeResultDTO = dResumeService.selectByExample(dResumeExample);
                if (resumeResultDTO.isSuccess()) {
                    searchResumeResultDTO = resumeConvertor.toDTO(resumeResultDTO.getData().get(0));
                    resumeConvertor.personInfoToDTO(searchResumeResultDTO,dPersonBase);
                    list.add(searchResumeResultDTO);

                }else{
                    log.error("查询简历基本信息失败，数据："+ resumeResultDTO.getData());
                }

                if (null != searchResumeResultDTO) {

                    DPersonFileExample dPersonFileExample = new DPersonFileExample();
                    dPersonFileExample.createCriteria().andDataFlagEqualTo(0)
                            .andPersonIdEqualTo(dPersonBase.getId());
                    ResultDTO<List<DPersonFile>> fileResultDTO = dPersonFileService.selectByExample(dPersonFileExample);
                    if (fileResultDTO.isSuccess()) {
                        resumeConvertor.personFileToDTO(searchResumeResultDTO,fileResultDTO.getData().get(0));
                    }else{
                        log.error("查询简历基本信息失败，数据："+ resumeResultDTO.getData());
                    }
                }
            }
            return ResultDTO.success(PageResultDTO.rows(resultDTO.getData().getTotal(), list));

        }else {
            log.error("查询人员基本信息失败，数据："+ resultDTO.getData());
        }
        return ResultDTO.failure(ResultError.error("请联系管理员，获取简历信息异常！"));
    }

    /**
     * @Author: ZhangRui
     * @param: multipartFile
     * @Description: 上传简历
     * @date: Created in 11:33 2018/8/16
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO uploadFile(File file,String fileName) {


        if (!file.exists()) {
            return ResultDTO.failure(ResultError.error("请联系管理员，目标文件不存在！"));
        }
        ImportExecl poi = new ImportExecl();
        Map<String,List<List<String>>> map = poi.read(file, file.getName());

        if (map.size() == 0) {
            log.error("读取excel时没有读取到数据！");
            return ResultDTO.failure(ResultError.error("简历模板异常，没有获取到相应信息！"));
        }
        //将简历sheet1中数据集合转换为键值对形式 name = 张三
        Map<String, Object> sheet1Map = sheet1ToMap(map.get("sheet1"));
        //将简历sheet2中数据集合转换为键值对形式
        Map<String, Object> sheet2Map = sheet2ToMap(map.get("sheet2"));

        //读取excel1转换dto
        ResultDTO baseRes = readExcelToBaseInfo(sheet1Map,sheet2Map);
        if (baseRes.isSuccess()) {
            //保存简历基本信息
            ResultDTO resumeResult = readExcelToResumeInfo(sheet1Map,(String) baseRes.getData());

            if (!resumeResult.isSuccess()) {
                //保存简历基本信息出错
                return resumeResult;
            }
            //保存候选人求职意向
            ResultDTO jobWantResult = readExcelToJobInfo(sheet2Map,(String) baseRes.getData());
            if (!jobWantResult.isSuccess()) {
                return jobWantResult;
            }

            ResultDTO otherResult = readExcelToOtherInfo(sheet2Map,(String) baseRes.getData());
            if (!otherResult.isSuccess()) {
                return otherResult;
            }

            ResultDTO workResult = readExcelToWorkInfo(sheet2Map,(String) baseRes.getData());
            if (!workResult.isSuccess()) {
                return workResult;
            }
            ResultDTO projectResult = readExcelToProjectInfo(sheet2Map,(String) baseRes.getData());
            if (!projectResult.isSuccess()) {
                return projectResult;
            }
            ResultDTO trainResult = readExcelToTrainInfo(sheet2Map,(String) baseRes.getData());
            if (!trainResult.isSuccess()) {
                return trainResult;
            }
            ResultDTO cardResult = readExcelToCardInfo(sheet2Map,(String) baseRes.getData());
            if (!cardResult.isSuccess()) {
                return cardResult;
            }

        }else{
            //保存人员基本信息出错
            return baseRes;
        }
        try {
            ResultDTO resultDTO = fileSdkService.uploadFile(file);
            if (resultDTO == null || !resultDTO.isSuccess()) {
                return ResultDTO.failure(ResultError.error("服务器上传附件错误！请联系管理员！"));
            } else {
                DPersonFile dPersonFile = new DPersonFile();
                Map<String, String> fileResult = (Map<String, String>)resultDTO.getData();
                dPersonFile.setFileName(fileName);
                dPersonFile.setFilePath(fileResult.get("url"));
                dPersonFile.setPersonId((String) baseRes.getData());
                ResultDTO insert = dPersonFileService.insertSelective(dPersonFile);
                if (insert == null || !insert.isSuccess()) {
                    return ResultDTO.failure(ResultError.error("附件上传返回内容处理失败！请联系管理员！"));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultDTO.failure(ResultError.error("服务器上传附件错误！请联系管理员！"));
        }
        return ResultDTO.success();
    }

    //region 将简历sheet1中数据集合转换为键值对形式
    /**
     * @Author: ZhangRui
     * @param: list 简历sheet1中数据
     * @Description:   将简历sheet1中数据集合转换为键值对形式
     * @date: Created in 17:05 2018/8/14
     */
    //endregion
    private Map<String, Object> sheet1ToMap(List<List<String>> list) {

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

    //region 将简历sheet2中数据集合转换为键值对形式
    /**
     * @Author: ZhangRui
     * @param: list 简历sheet2中数据
     * @Description:   将简历sheet2中数据集合转换为键值对形式
     * @date: Created in 17:05 2018/8/14
     */
    //endregion
    private Map<String, Object> sheet2ToMap(List<List<String>> list) {

        Map<String, Object> map = new HashMap<String, Object>();

        //不同阶段值不同
        int flag = 0;
        //工作经历
        int indexWork = 0;
        List<Map<String, String>> workList = new ArrayList<>();
        map.put("workExperience", workList);
        Map<String, String> workMap = new HashMap<>();
        //项目经验
        List<Map<String, String>> projectList = new ArrayList<>();
        map.put("projectExperience", projectList);
        Map<String, String> projectMap = new HashMap<>();
        //教育经历
        int indexEducation = 0;
        StringBuilder educationBuilder = new StringBuilder();
        //培训经历
        List<Map<String, String>> trainList = new ArrayList<>();
        map.put("trainExperience", trainList);
        Map<String, String> trainMap = new HashMap<>();
        //证书
        int indexCard = 0;
        List<Map<String, String>> cardList = new ArrayList<>();
        map.put("card", cardList);
        Map<String, String> cardMap = new HashMap<>();
        //在校学习情况
        int indexLearn = 0;
        StringBuilder learnBuilder = new StringBuilder();
        //在校实践经验
        int indexSocial = 0;
        StringBuilder socialBuilder = new StringBuilder();
        //语言能力
        int indexLanguage = 0;
        StringBuilder languageBuilder = new StringBuilder();
        //专业技能
        int indexSkill = 0;
        StringBuilder skillBuilder = new StringBuilder();
        //兴趣爱好
        int indexHobby = 0;
        StringBuilder hobbyBuilder = new StringBuilder();

        for(List<String> strList : list){
            String key = strList.get(0);
            String val = strList.get(1);
            if (StringUtils.isNotEmpty(key) && TotalTagsEnum.getValue(key) != null) {
                flag = TotalTagsEnum.getValue(key);
            }
            //人员基本信息
            if (flag == 0 && StringUtils.isNotEmpty(BaseTagEnum.getValue(key))) {
                map.put(BaseTagEnum.getValue(key), val);
            }
            //求职意向
            if (flag == 1 && StringUtils.isNotEmpty(CareerTagEnum.getValue(key))) {
                map.put(CareerTagEnum.getValue(key), val);
            }
            //自我评价
            if (flag == 2 && StringUtils.isEmpty(key)) {
                map.put("content", val);
            }
            //工作经历
            if (flag == 3) {
                if (indexWork == 0) {
                    indexWork++;
                } else if (indexWork == 1) {
                    workMap.put("startDate", val);
                    indexWork++;
                } else if (indexWork == 2) {
                    workMap.put("salary", val);
                    indexWork++;
                }else if (indexWork == 3){
                    workMap.put("content", val);
                    indexWork++;
                }else if (indexWork == 4){
                    indexWork = 1;
                    workMap.put("position", val);
                    workList.add(workMap);
                    workMap = new HashMap<>();
                }
            }
            //项目经验
            if (flag == 5) {
                projectMap = new HashMap<>();
                projectList.add(projectMap);
                projectMap.put(ProjectTagEnum.getValue(key), val);
            } else if (flag == 6) {
                projectMap.put(ProjectTagEnum.getValue(key), val);
            } else if (flag == 7) {
                projectMap.put(ProjectTagEnum.getValue(key), val);
            }
            //教育经历
            if (flag == 8 && indexEducation == 0) {
                indexEducation++;
            }else if (flag == 8 && indexEducation == 1) {
                educationBuilder.append("\r\n"+val);
            }
            if (flag == 9) {
                if (educationBuilder.toString().length() >= 2) {
                    map.put("educationBackground", educationBuilder.toString().substring(2));
                }
            }
            //培训经历
            if (flag == 10) {
                trainMap = new HashMap<>();
                trainList.add(trainMap);
                trainMap.put(TrainTagEnum.getValue(key), val);
            } else if (flag == 11) {
                trainMap.put(TrainTagEnum.getValue(key), val);
            } else if (flag == 12) {
                trainMap.put(TrainTagEnum.getValue(key), val);
            } else if (flag == 13) {
                trainMap.put(TrainTagEnum.getValue(key), val);
            }
            //证书
            if (flag == 14) {
                if (indexCard == 0) {
                    indexCard++;
                } else if (indexCard == 1) {
                    cardMap.put("cardName", val);
                    indexCard++;
                } else if (indexCard == 2) {
                    indexCard = 1;
                    cardList.add(cardMap);
                    cardMap = new HashMap<>();
                }
            }

            //在校学习情况
            if (flag == 15 && indexLearn == 0) {
                indexLearn++;
            }else if (flag == 15 && indexLearn == 1) {
                learnBuilder.append("\r\n"+val);
            }
            //在校实践经验
            if (flag == 16 && indexSocial == 0) {
                if (learnBuilder.toString().length() >= 2) {
                    map.put("learnSituation", learnBuilder.toString().substring(2));
                }
                indexSocial++;
            }else if (flag == 16 && indexSocial == 1) {
                socialBuilder.append("\r\n"+val);
            }
            //语言能力
            if (flag == 17 && indexLanguage == 0) {
                if (socialBuilder.toString().length() >= 2) {
                    map.put("socialSituation", socialBuilder.toString().substring(2));
                }
                indexLanguage++;
            }else if (flag == 17 && indexLanguage == 1) {
                languageBuilder.append("\r\n"+val);
            }
            //专业技能
            if (flag == 18 && indexSkill == 0) {
                if (languageBuilder.toString().length() >= 2) {
                    map.put("languageSituation", languageBuilder.toString().substring(2));
                }
                indexSkill++;
            }else if (flag == 18 && indexSkill == 1) {
                skillBuilder.append("\r\n"+val);
            }
            //兴趣爱好
            if (flag == 19 && indexHobby == 0) {
                if (skillBuilder.toString().length() >= 2) {
                    map.put("professionalSkills", skillBuilder.toString().substring(2));
                }
                indexHobby++;
            }else if (flag == 19 && indexHobby == 1) {
                hobbyBuilder.append(val);
                map.put("hobby", hobbyBuilder.toString());
            }

        }

        return map;
    }

    //region 读取Excel候选人基本信息
    /**
     * 读取Excel候选人基本信息
     * @param param
     * @return
     */
    //endregion
    private ResultDTO readExcelToBaseInfo(Map<String, Object> param,Map<String, Object> map) {

        DPersonBase dPersonBase = new DPersonBase();

        //sheet1中信息转换
        ResultDTO result = mapToEntity(dPersonBase, param);
        if (!result.isSuccess()) {
            return result;
        }
        //sheet2中部分信息转换
        mapToEntity(dPersonBase, map);

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

    //region 读取Excel求职意向信息
    /**
     * 读取Excel求职意向信息
     * @param param
     * @return
     */
    //endregion
    private ResultDTO readExcelToJobInfo(Map<String, Object> param,String personId) {

        DJobWant dJobWant = new DJobWant();
        //转换
        ResultDTO result = mapToEntity(dJobWant, param);
        if (!result.isSuccess()) {
            return result;
        }
        dJobWant.setPersonId(personId);
        ResultDTO<String> resultDTO = dJobWantService.insertSelective(dJobWant);
        if (resultDTO.isSuccess()) {
            return resultDTO;
        }else {

            return ResultDTO.failure(ResultError.error("请联系管理员，职意向信息入库失败！"));
        }

    }

    //region 读取Excel候选人其他附属信息
    /**
     * 读取Excel候选人其他附属信息
     * @param param
     * @return
     */
    //endregion
    private ResultDTO readExcelToOtherInfo(Map<String, Object> param,String personId) {

        DPersonOther dPersonOther = new DPersonOther();
        //转换
        ResultDTO result = mapToEntity(dPersonOther, param);
        if (!result.isSuccess()) {
            return result;
        }
        dPersonOther.setPersonId(personId);
        ResultDTO<String> resultDTO = dPersonOtherService.insertSelective(dPersonOther);
        if (resultDTO.isSuccess()) {
            return resultDTO;
        }else {

            return ResultDTO.failure(ResultError.error("请联系管理员，候选人其他附属信息入库失败！"));
        }

    }

    //region 读取Excel候选人工作经历信息
    /**
     * 读取Excel候选人工作经历信息
     * @param param
     * @return
     */
    //endregion
    private ResultDTO readExcelToWorkInfo(Map<String, Object> param,String personId) {

        if (param.get("workExperience") != null) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("workExperience");
            for (Map<String, Object> map : list) {

                DWorkExperience dWorkExperience = new DWorkExperience();
                //转换
                ResultDTO result = mapToEntity(dWorkExperience, map);
                if (!result.isSuccess()) {
                    return result;
                }
                dWorkExperience.setPersonId(personId);
                ResultDTO<String> resultDTO = dWorkExperienceService.insertSelective(dWorkExperience);
                if (!resultDTO.isSuccess()) {
                    return ResultDTO.failure(ResultError.error("请联系管理员，候选人工作经历信息入库失败！"));
                }
            }
        }
        return ResultDTO.success();

    }

    //region 读取Excel候选人项目经验信息
    /**
     * 读取Excel候选人项目经验信息
     * @param param
     * @return
     */
    //endregion
    private ResultDTO readExcelToProjectInfo(Map<String, Object> param,String personId) {

        if (param.get("projectExperience") != null) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("projectExperience");
            for (Map<String, Object> map : list) {

                DProjectExperience dProjectExperience = new DProjectExperience();
                //转换
                ResultDTO result = mapToEntity(dProjectExperience, map);
                if (!result.isSuccess()) {
                    return result;
                }
                dProjectExperience.setPersonId(personId);
                ResultDTO<String> resultDTO = dProjectExperienceService.insertSelective(dProjectExperience);
                if (!resultDTO.isSuccess()) {
                    return ResultDTO.failure(ResultError.error("请联系管理员，候选人项目经验信息入库失败！"));
                }
            }
        }
        return ResultDTO.success();

    }

    //region 读取Excel候选人培训经历信息
    /**
     * 读取Excel候选人培训经历信息
     * @param param
     * @return
     */
    //endregion
    private ResultDTO readExcelToTrainInfo(Map<String, Object> param,String personId) {

        if (param.get("trainExperience") != null) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("trainExperience");
            for (Map<String, Object> map : list) {

                DTrainExperience dTrainExperience = new DTrainExperience();
                //转换
                ResultDTO result = mapToEntity(dTrainExperience, map);
                if (!result.isSuccess()) {
                    return result;
                }
                dTrainExperience.setPersonId(personId);
                ResultDTO<String> resultDTO = dTrainExperienceService.insertSelective(dTrainExperience);
                if (!resultDTO.isSuccess()) {
                    return ResultDTO.failure(ResultError.error("请联系管理员，培训经历信息入库失败！"));
                }
            }
        }
        return ResultDTO.success();

    }

    //region 读取Excel候选人证书信息
    /**
     * 读取Excel候选人证书信息
     * @param param
     * @return
     */
    //endregion
    private ResultDTO readExcelToCardInfo(Map<String, Object> param,String personId) {

        if (param.get("card") != null) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("card");
            for (Map<String, Object> map : list) {

                DCards dCards = new DCards();
                //转换
                ResultDTO result = mapToEntity(dCards, map);
                if (!result.isSuccess()) {
                    return result;
                }
                dCards.setPersonId(personId);
                ResultDTO<String> resultDTO = dCardsService.insertSelective(dCards);
                if (!resultDTO.isSuccess()) {
                    return ResultDTO.failure(ResultError.error("请联系管理员，候选人证书信息入库失败！"));
                }
            }
        }
        return ResultDTO.success();

    }

    //region 读取Excel简历基本信息
    /**
     * 读取Excel简历基本信息
     * @param param
     * @return
     */
    //endregion
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
