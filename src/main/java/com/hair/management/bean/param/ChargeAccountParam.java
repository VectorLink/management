package com.hair.management.bean.param;

import com.hair.management.bean.enumerate.ConsumerType;
import lombok.Data;

/**
 * 变化账户金额类型
 */
@Data
public class ChargeAccountParam {
    ConsumerType consumerType;
    String  alterAmount;
    Long userId;
}
