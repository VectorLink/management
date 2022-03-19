package com.hair.management.bean.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListResp<T> {
    /**
     * 分页参数
     */
    private Page page;
    /**
     * 集合
     */
    private List<T> data;
}
