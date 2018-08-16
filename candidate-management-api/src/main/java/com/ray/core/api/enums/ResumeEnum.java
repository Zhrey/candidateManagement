package com.ray.core.api.enums;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 14:20 2018/8/9
 * @Modified By:
 */
public enum ResumeEnum {

    RESUME_NAME("简历名称","resumeName"),
    TAG_NAME("标签名称","tagName"),
    JOB_POSITION("应聘职位","jobPosition"),
    CURRENT_COMPANY("现在单位","currentCompany"),
    EXPECT_SALARY("期望月薪(税前)","expectSalary"),
    DELIVERY_DATE("投递(收藏)时间","deliveryDate"),
    WORK_YEARS("工作年限","workYears");

    // 成员变量
    private String name;
    private String value;

    ResumeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getValue(String name) {

        for (ResumeEnum personBaseEnum : ResumeEnum.values()) {

            if (personBaseEnum.name.equals(name)) {
                return personBaseEnum.value;
            }

        }
        return null;
    }
}
