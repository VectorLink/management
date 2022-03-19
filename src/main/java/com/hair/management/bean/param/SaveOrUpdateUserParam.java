package com.hair.management.bean.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveOrUpdateUserParam {
    /**
     * 用户ID
     */
    private Long userId;

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
}
