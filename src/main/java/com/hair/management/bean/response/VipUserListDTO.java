package com.hair.management.bean.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VipUserListDTO {
    /**
     * 会员编号ID
     */
    public Long vipUserId;
    /**
     *会员名称
     */
    public String vipUserName;
    /**
     * 性别
     */
    public String sex;
    /**
     * 电话
     */
    public String telephone;
    /**
     * 发型师ID
     */
    public Long hairMasterId;
    /**
     * 发型师名称
     */
    public String hairMasterName;
    /**
     * 创建时间
     */
    public String createTime;
    /**
     * 账户余额
     */
    public String accountAmount;
}
