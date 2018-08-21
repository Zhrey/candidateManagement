package com.ray.core.api.enums;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 14:20 2018/8/9
 * @Modified By:
 */
public enum TrainTagEnum {

    TRAIN_NAME("培训机构：","trainName"),
    LOCATION("培训地点：","location"),
    CARD("所获证书：","card"),
    CONTENT("培训描述：","content");

    // 成员变量
    private String name;
    private String value;

    TrainTagEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getValue(String name) {

        for (TrainTagEnum baseTagEnum : TrainTagEnum.values()) {

            if (baseTagEnum.name.equals(name)) {
                return baseTagEnum.value;
            }

        }
        return null;
    }
}
