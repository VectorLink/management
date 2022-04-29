package com.hair.management.bean.enumerate;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChangeConsumerBillParam {
    /**
     * 消费主键
     */
    private Long consumerId;
    /**
     * 修改金额
     */
    private BigDecimal changeAmount;
}
