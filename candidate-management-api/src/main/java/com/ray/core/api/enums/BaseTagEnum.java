package com.ray.core.api.enums;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 14:20 2018/8/9
 * @Modified By:
 */
public enum BaseTagEnum {

    MARITAL_STATUS("婚姻状况","maritalStatus"),
    POLITICAL_STATUS("政治面貌","politicalStatus"),
    IDENTITY_ID("身份证","identityId"),
    POSTAL_CODE("邮编","postalCode");

    // 成员变量
    private String name;
    private String value;

    BaseTagEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getValue(String name) {

        for (BaseTagEnum baseTagEnum : BaseTagEnum.values()) {

            if (baseTagEnum.name.equals(name)) {
                return baseTagEnum.value;
            }

        }
        return null;
    }
}
