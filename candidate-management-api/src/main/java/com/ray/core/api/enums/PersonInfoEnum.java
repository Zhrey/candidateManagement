package com.ray.core.api.enums;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 14:20 2018/8/9
 * @Modified By:
 */
public enum PersonInfoEnum {

    MARITAL_STATUS("婚姻状况","maritalStatus"),
    POLITICAL_STATUS("政治面貌","politicalStatus"),
    IDENTITY_ID("身份证","identityId"),
    POSTAL_CODE("邮编","postalCode");

    // 成员变量
    private String name;
    private String value;

    PersonInfoEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getValue(String name) {

        for (PersonInfoEnum personInfoEnum : PersonInfoEnum.values()) {

            if (personInfoEnum.name.equals(name)) {
                return personInfoEnum.value;
            }

        }
        return null;
    }
}
