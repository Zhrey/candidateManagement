package com.ray.core.api.enums;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 14:20 2018/8/9
 * @Modified By:
 */
public enum ProjectTagEnum {

    PROJECT_NAME("项目名称：","projectName"),
    CONTENT("项目描述：","content"),
    DUTY_CONTENT("责任描述：","dutyContent");

    // 成员变量
    private String name;
    private String value;

    ProjectTagEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getValue(String name) {

        for (ProjectTagEnum baseTagEnum : ProjectTagEnum.values()) {

            if (baseTagEnum.name.equals(name)) {
                return baseTagEnum.value;
            }

        }
        return null;
    }
}
