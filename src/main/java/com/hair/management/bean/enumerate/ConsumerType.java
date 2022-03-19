package com.hair.management.bean.enumerate;

/**
 * 消费类型配置
 */
public enum ConsumerType {
    normal_consumer("普通消费"),
    charge("客户充值");
    private String displayName;
    ConsumerType(String displayName) {
        this.displayName = displayName;
    }


    /**
     * 获取消费类型
     * @param ordinal
     * @return
     */
    public static String getDisplayNameByOrdinal(Integer ordinal){
        for (ConsumerType value : ConsumerType.values()) {
            if (ordinal.equals(value.ordinal())){
                return value.displayName;
            }
        }
        return normal_consumer.displayName;
    }
}
