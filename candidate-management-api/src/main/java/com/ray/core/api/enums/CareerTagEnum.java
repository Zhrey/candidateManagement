package com.ray.core.api.enums;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 14:20 2018/8/9
 * @Modified By:
 */
public enum CareerTagEnum {

    DESIRED_NATRUE("期望工作性质：","desiredNatrue"),
    DESIRED_POSITION("期望从事职业：","desiredPosition"),
    DESIRED_INDUSTRY("期望从事行业：","desiredIndustry"),
    DESIRED_ADDRESS("期望工作地区：","desiredAddress"),
    CURRENT_SITUATION("目前状况：","currentSituation");

    // 成员变量
    private String name;
    private String value;

    CareerTagEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getValue(String name) {

        for (CareerTagEnum baseTagEnum : CareerTagEnum.values()) {

            if (baseTagEnum.name.equals(name)) {
                return baseTagEnum.value;
            }

        }
        return null;
    }
}
