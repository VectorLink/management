package com.hair.management.bean.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 当日消息信息统计
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatisticDTO implements Serializable {
    /**
     * 今日充值金额
      */
    BigDecimal chargeAmount;
    /**
     * 今日会员消费金额
     */
    BigDecimal consumerAmount;
    /**
     * 会员进店消费数量
     */
    Integer userTotal;
}
