package com.hair.management.bean.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveOrUpdateUserParam {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 会员编号
     */
    private Long userCode;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 发型师ID
     */
    private Long hairMasterId;
    /**
     * 账户金额
     */
    private BigDecimal accountAmount;
}
