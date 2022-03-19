package com.hair.management.bean.enumerate;

/**
 * 性别枚举
 */
public enum SexEnum {
    male("男士")
    ,female("女士");

    SexEnum(String displayName) {
        this.displayName = displayName;
    }

    private String displayName;

    /**
     * 获取性别枚举
     * @param ordinal
     * @return
     */
    public static String getDisplayNameByOrdinal(Integer ordinal){
        for (SexEnum value : SexEnum.values()) {
            if (ordinal.equals(value.ordinal())){
                return value.displayName;
            }
        }
        return male.displayName;
    }
}
