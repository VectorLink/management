package com.hair.management.bean.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class HairMasterProfile implements Serializable {
    /**
     * Id
     */
    private Long hairMasterId;

    /**
     * 发型师名称
     */
    private String hairMasterName;

    /**
     * 发型师登录名
     */
    private String hairMasterCode;

    /**
     * 用户类型，默认为普通发型师
     */
    private Integer type;

    public Long getId() {
        return hairMasterId;
    }
}
