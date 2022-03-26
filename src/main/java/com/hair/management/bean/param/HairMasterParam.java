package com.hair.management.bean.param;

import com.hair.management.bean.response.Page;
import lombok.Data;

@Data
public class HairMasterParam {
    private String hairMasterCode;
    private String hairMasterName;
    private Page page;
}
