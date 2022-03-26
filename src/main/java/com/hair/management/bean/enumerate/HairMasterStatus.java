package com.hair.management.bean.enumerate;

public enum HairMasterStatus {
    DISABLE("禁用"),
    USING("启用");
    private String displayName;

    HairMasterStatus(String displayName) {
        this.displayName = displayName;
    }
}
