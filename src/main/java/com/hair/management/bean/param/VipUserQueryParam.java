package com.hair.management.bean.param;

import com.hair.management.bean.response.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VipUserQueryParam {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 电话号码
     */
    private String telephone;
    /**
     * 分页参数
     */
    Page page;
}
