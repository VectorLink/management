package com.hair.management.bean.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class HairMasterDTO {
    private Long hairMasterId;

    /**
     * 发型师名称
     */
    private String hairMasterName;
}
