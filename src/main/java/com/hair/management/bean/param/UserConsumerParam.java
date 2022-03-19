package com.hair.management.bean.param;

import com.hair.management.bean.response.Page;
import lombok.Data;

@Data
public class UserConsumerParam {
    Page page;
    Long userId;

}
