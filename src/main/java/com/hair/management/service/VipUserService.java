package com.hair.management.service;

import com.hair.management.bean.param.SaveOrUpdateUserParam;
import com.hair.management.bean.param.VipUserQueryParam;
import com.hair.management.bean.response.VipUserListDTO;
import com.hair.management.bean.response.UserListResp;
import com.hair.management.dao.entity.VipUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2022-03-19
 */
public interface VipUserService extends IService<VipUser> {
    /**
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    Boolean saveOrUpdateVipUser(SaveOrUpdateUserParam param);

    /**
     * 电话号码是否已经被注册了
     * @param tel 电话号码
     * @return
     */
    Boolean isTelNumRegistered(String tel);

    /**
     * 分页查询VIP刻画
     * @param param 参数
     * @return
     */
    UserListResp<VipUserListDTO> listVipUserList(VipUserQueryParam param);
}
