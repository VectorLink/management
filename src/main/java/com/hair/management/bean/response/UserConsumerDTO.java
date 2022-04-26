package com.hair.management.bean.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserConsumerDTO {

    /**
     * 消费主键
     */
    private Long id;
    /**
     * 消费类型
     */
    private String consumerType;

    /**
     * 会员ID
     */
    private Long vipUserId;
    /**
     * 会员名称
     */
    private String vipUserName;

    /**
     * 消费前账户金额
     */
    private BigDecimal preAccountAmount;

    /**
     * 消费金额
     */
    private BigDecimal consumerAmount;

    /**
     * 消费后金额
     */
    private BigDecimal afterAccountAmount;

    /**
     * 消费时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime consumerTime;
    /**
     * 通知客户类型
     */
    private String noticeUserType;
    /**
     * 是否已经通知用户
     */
    private Boolean noticeUser;
    /**
     * 发艺师名称
     */
    private String hairMasterName;
    /**
     * 签名
     */
    private byte[] signImg;
}
