package com.ray.core.api.service.impl;

import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.file.sdk.service.FileSdkService;
import com.ray.cloud.framework.mybatis.entity.DPersonBase;
import com.ray.cloud.framework.mybatis.service.DPersonBaseService;
import com.ray.core.api.enums.PersonBaseEnum;
import com.ray.core.api.service.ResumeService;
import com.ray.core.api.utils.ImportExecl;
import com.ray.core.api.utils.WDWUtil;
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

    public ResultDTO downloadFile(String filePath, String fileName){

        return fileSdkService.downloadFile(filePath,fileName);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultDTO uploadFile(File file) {

        try {

            if (!file.exists()) {
                throw new RuntimeException("目标文件不存在！");
            }
            ImportExecl poi = new ImportExecl();
            Map<String,List<List<String>>> map = poi.read(file, file.getName());

            if (map.size() == 0) {
                log.error("读取excel时没有读取到数据！");
            }
            //将简历sheet1中数据集合转换为键值对形式
            Map<String, Object> baseMap = listToMap(map.get("sheet1"));
            //读取excel转换dto
            readResumeToBaseInfo(baseMap);


        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
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
            String key = PersonBaseEnum.getValue(keyList.get(i));
            if (StringUtils.isNotEmpty(key)) {
                map.put(key, valList.get(i));
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
    private ResultDTO readResumeToBaseInfo(Map<String, Object> param) {

        DPersonBase dPersonBase = new DPersonBase();

        if (param != null) {
            Field[] fields = dPersonBase.getClass().getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);
                try {
                    if (StringUtils.isEmpty((String) param.get(field.getName()))) {
                        continue;
                    }
                    if (field.getType() == Integer.class) {
                    } else if (field.getType() == BigDecimal.class) {
                        field.set(dPersonBase, new BigDecimal((String) param.get(field.getName())));
                    } else if (field.getType() == Date.class) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
                        Date birthday = simpleDateFormat.parse((String) param.get(field.getName()));
                        field.set(dPersonBase, birthday);
                    } else {
                        log.info(field.getName());
                        log.info(WDWUtil.ran2fuc((String) param.get(field.getName())));
                        field.set(dPersonBase, WDWUtil.ran2fuc((String) param.get(field.getName())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("赋值错误：" + e.getMessage(), e);
                    return ResultDTO.failure();
                }
            }
        } else {
            log.error("companyInfoStringList：null");
            return ResultDTO.failure();
        }
        dPersonBaseService.insertSelective(dPersonBase);

        return ResultDTO.success(dPersonBase);
    }
}
