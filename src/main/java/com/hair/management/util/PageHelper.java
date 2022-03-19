package com.hair.management.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hair.management.bean.response.Page;

public class PageHelper {
    public static Page setResponsePage(IPage page) {
        return Page.builder().current(page.getCurrent())
                .size(page.getSize())
                .total(page.getTotal())
                .pages(page.getPages())
                .build();
    }
}
