package com.hair.management.dao.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 会员信息消费信息表
 * </p>
 *
 * @author ${author}
 * @since 2022-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_consumer_info")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserConsumerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消费类型
     */
    private Integer consumerType;

    /**
     * 会员名
     */
    private Long vipUserId;

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
    private LocalDateTime consumerTime;
    /**
     * 通知客户类型
     */
    private Integer noticeUserType;
    /**
     * 是否已经通知用户
     */
    private Boolean noticeUser;
    /**
     * 发艺师ID
     */
    private Long hair_master_id;
}