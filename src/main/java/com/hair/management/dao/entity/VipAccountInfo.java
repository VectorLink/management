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
 * 会员账户信息表
 * </p>
 *
 * @author ${author}
 * @since 2022-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vip_account_info")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VipAccountInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户ID
     */
      @TableId(value = "account_id", type = IdType.AUTO)
    private Long accountId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 账户金额
     */
    private BigDecimal accountAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
