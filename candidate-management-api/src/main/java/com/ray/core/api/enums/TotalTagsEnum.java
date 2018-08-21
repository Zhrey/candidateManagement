package com.ray.core.api.enums;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 14:20 2018/8/9
 * @Modified By:
 */
public enum TotalTagsEnum {

    BASE_TAG("个人基本信息",0),
    CAREER_TAG("求职意向",1),
    SELF_TAG("自我评价",2),
    WORK_TAG("工作经历",3),
    PROJECT_TAG("项目经验",4),
    PROJECT_NAME("项目名称：",5),
    RESPONSIBILITY_CONTENT("责任描述：",6),
    PROJECT_CONTENT("项目描述：",7),
    EDUCATION_TAG("教育经历",8),
    TRANING_TAG("培训经历",9),
    INSTITUTION("培训机构：",10),
    LOCATION("培训地点：",11),
    CERTIFICATE("所获证书：",12),
    DESCRIPTION("培训描述：",13),
    CERTIFICATE_TAG("证书",14),
    GRADUATES_TAG("在校学习情况",15),
    SOCIAL_TAG("在校实践经验",16),
    LANGUAGE_TAG("语言能力",17),
    PROFESSIONAL_TAG("专业技能",18),
    HOBBY_TAG("兴趣爱好",19);

    // 成员变量
    private String name;
    private Integer value;

    TotalTagsEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static Integer getValue(String name) {

        for (TotalTagsEnum totalTagsEnum : TotalTagsEnum.values()) {

            if (totalTagsEnum.name.equals(name)) {
                return totalTagsEnum.value;
            }

        }
        return null;
    }
}
