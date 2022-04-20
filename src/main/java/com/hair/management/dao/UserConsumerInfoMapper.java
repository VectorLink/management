package com.hair.management.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hair.management.bean.response.UserConsumerDTO;
import com.hair.management.dao.entity.UserConsumerInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 会员信息消费信息表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2022-03-19
 */
public interface UserConsumerInfoMapper extends BaseMapper<UserConsumerInfo> {
    /**
     * 分页查询 客户消费记录
     * @param page
     * @param userId
     * @return
     */
    IPage<UserConsumerDTO> getUserConsumerInfoByUserId(Page page,@Param("userId") Long userId);

    /**
     * 分页查询 发型师账目
     * @param page
     * @param hairMasterId
     * @return
     */
    IPage<UserConsumerDTO> getMasterBillByMasterId(Page page,@Param("hairMasterId") Long hairMasterId);
}
