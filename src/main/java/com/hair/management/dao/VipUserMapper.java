package com.hair.management.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hair.management.bean.param.VipUserQueryParam;
import com.hair.management.bean.response.VipUserListDTO;

import com.hair.management.dao.entity.VipUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2022-03-19
 */
public interface VipUserMapper extends BaseMapper<VipUser> {

    IPage<VipUserListDTO> listVipUserList(Page page, @Param("param") VipUserQueryParam param);
}
