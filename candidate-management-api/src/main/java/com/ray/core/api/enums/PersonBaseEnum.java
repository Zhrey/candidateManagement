package com.ray.core.api.enums;

import lombok.Getter;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 14:20 2018/8/9
 * @Modified By:
 */
@Getter
public enum PersonBaseEnum {

    NAME("姓名","name"),
    SEX("性别","sex"),
    BIRTHDAY("出生日期","birthday"),
    WORKINGTIME("工作年限","workingTime"),
    MOBILE("移动电话","mobile"),
    MAIL("电子邮件","mail"),
    CURRENTADDRESS("目前居住地","currentAddress"),
    POSTALADDRESS("通讯地址","postalAddress"),
    HUKOU("户口","hukou"),
    MAJOR("专业名称","major"),
    SCHOOL("学校名称","school"),
    MAXEDUCATION("最高学历","maxEducation");

    // 成员变量
    private String name;
    private String value;

    PersonBaseEnum (String name,String value) {
        this.name = name;
        this.value = value;
    }

    public static String getValue(String name) {

        for (PersonBaseEnum personBaseEnum : PersonBaseEnum.values()) {

            if (personBaseEnum.name.equals(name)) {
                return personBaseEnum.value;
            }

        }
        return null;
    }
}
