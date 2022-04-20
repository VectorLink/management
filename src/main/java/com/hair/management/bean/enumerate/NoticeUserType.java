package com.hair.management.bean.enumerate;

public enum NoticeUserType {
    MESSAGE("短信"),
    NO_VIP_NOT_NOTICE("非会员不通知");
    private String displayName;

    NoticeUserType(String displayName) {
        this.displayName = displayName;
    }
    /**
     * 获取通知类型
     * @param ordinal
     * @return
     */
    public static String getDisplayNameByOrdinal(Integer ordinal){
        for (NoticeUserType value : NoticeUserType.values()) {
            if (ordinal.equals(value.ordinal())){
                return value.displayName;
            }
        }
        return MESSAGE.displayName;
    }
}
