package com.hair.management.service;

import com.hair.management.bean.param.ChargeAccountParam;
import com.hair.management.dao.entity.VipAccountInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 会员账户信息表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2022-03-19
 */
public interface VipAccountInfoService extends IService<VipAccountInfo> {
    /**
     * 根据会员ID增加对应的账户信息
     * @param vipUserId 用户ID
     * @return 添加是否成功
     */
    Boolean addVipAccountInfo(Long vipUserId);

    /**
     * 更改账户 余额
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    String changeAccountByUserId(ChargeAccountParam param);
}
