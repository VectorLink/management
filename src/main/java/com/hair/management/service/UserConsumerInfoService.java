package com.hair.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hair.management.bean.enumerate.ChangeConsumerBillParam;
import com.hair.management.bean.param.StatisticParam;
import com.hair.management.bean.param.UserConsumerParam;
import com.hair.management.bean.response.AllUserStatisticDto;
import com.hair.management.bean.response.UserConsumerDTO;
import com.hair.management.bean.response.UserListResp;
import com.hair.management.bean.response.UserStatisticDTO;
import com.hair.management.dao.entity.UserConsumerInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会员信息消费信息表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2022-03-19
 */
public interface UserConsumerInfoService extends IService<UserConsumerInfo> {
    /**
     * 分页查询 客户消费记录
     * @return
     */
    UserListResp<UserConsumerDTO> getUserConsumerInfoByUserId(UserConsumerParam param);

    UserListResp<UserConsumerDTO> getMasterBillByMasterId(UserConsumerParam param);

    /**
     * 获取当前日期的消费数据
     * @return
     */
    UserStatisticDTO getHairMasterStatistic(StatisticParam param);

    List<AllUserStatisticDto> getAllStatistic(StatisticParam param);

    /**
     * 修改消费信息
     * @param param
     * @return
     */
    void changeConsumerBill(ChangeConsumerBillParam param);
}
