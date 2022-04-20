package com.hair.management.bean.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 所有统计信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllUserStatisticDto implements Serializable {
    /**
     * 发型师名称
     */
    String hairMasterName;
    /**
     * 会员消费信息
     */
    UserStatisticDTO vipStatistic;
    /**
     * 非会员统计信息
     */
    UserStatisticDTO notVipStatistic;
}
