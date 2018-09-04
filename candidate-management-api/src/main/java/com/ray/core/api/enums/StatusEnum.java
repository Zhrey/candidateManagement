package com.ray.core.api.enums;

import lombok.Getter;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 14:20 2018/8/9
 * @Modified By:
 */
@Getter
public enum StatusEnum {

    NO_CANTACT("未沟通",0),
    CANTACTING("沟通中",1),
    FIRST_INTERVIEW("初面",2),
    SECOND_INTERVIEW("终面",3),
    GIVE_UP("放弃",4),
    ENTRY("入职",5),
    QUIT("离职",6);

    // 成员变量
    private String name;
    private int value;

    StatusEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static int getValue(String name) {

        for (StatusEnum baseTagEnum : StatusEnum.values()) {

            if (baseTagEnum.name.equals(name)) {
                return baseTagEnum.value;
            }

        }
        return 0;
    }
}
