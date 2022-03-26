package com.hair.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hair.management.bean.param.HairMasterParam;
import com.hair.management.bean.response.HairMasterDTO;
import com.hair.management.dao.entity.HairMaster;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 发型师表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2022-03-20
 */
public interface HairMasterService extends IService<HairMaster> {
    /**
     * 根据登录名获取对应的发型师
     * @param code
     * @return
     */
    HairMaster getByCode(String code);

    /**
     * 获取当前登录人员
     * @return
     */
    HairMaster getCurrentHairMaster();

    /**
     * 批量获取对应的发型师
     * @return
     */
    List<HairMasterDTO> getAllHairMaster();

    /**
     * 获取发型师信息
     * @param param
     * @return
     */
    IPage<HairMaster> listHairMasters(HairMasterParam param);

    /**
     * 添加或者修改发型师
     * @param hairMaster
     * @return
     */
    Boolean saveOrUpdateMaster(HairMaster hairMaster);

    /**
     * 根据CODE获取对应的发型师
     * @param hairMasterCode
     * @return
     */
    HairMaster getHairMasterByCode(String hairMasterCode);
}
