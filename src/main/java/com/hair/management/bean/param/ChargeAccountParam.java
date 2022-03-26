package com.hair.management.bean.param;

import com.hair.management.bean.enumerate.ConsumerType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 变化账户金额类型
 */
@Data
public class ChargeAccountParam {
    ConsumerType consumerType;
    BigDecimal alterAmount;
    Long userId;
}
