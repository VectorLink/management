package com.hair.management.bean.enumerate;

public enum HairMasterType {
    ADMIN("管理员"),
    HAIR_MASTER("理发师");
    private String displayName;

    HairMasterType(String displayName) {
        this.displayName = displayName;
    }
}
