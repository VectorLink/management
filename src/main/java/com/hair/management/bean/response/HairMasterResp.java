package com.hair.management.bean.response;

import com.hair.management.dao.entity.HairMaster;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HairMasterResp {
    List<HairMaster> data;
    Page page;
}
