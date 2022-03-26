package com.hair.management.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hair.management.bean.Constants;
import com.hair.management.bean.enumerate.ConsumerType;
import com.hair.management.bean.enumerate.NoticeUserType;
import com.hair.management.bean.param.UserConsumerParam;
import com.hair.management.bean.response.UserConsumerDTO;
import com.hair.management.bean.response.UserListResp;
import com.hair.management.dao.entity.UserConsumerInfo;
import com.hair.management.dao.UserConsumerInfoMapper;
import com.hair.management.service.UserConsumerInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hair.management.util.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

/**
 * <p>
 * 会员信息消费信息表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2022-03-19
 */
@Service
public class UserConsumerInfoServiceImpl extends ServiceImpl<UserConsumerInfoMapper, UserConsumerInfo> implements UserConsumerInfoService {

    @Override
    public UserListResp<UserConsumerDTO> getUserConsumerInfoByUserId(UserConsumerParam param) {
        Page page=new Page(param.getPage().getCurrent(),param.getPage().getSize());
        page.addOrder(OrderItem.desc("uci.consumer_time"));
        IPage<UserConsumerDTO> userConsumerInfo = this.baseMapper.getUserConsumerInfoByUserId(page, param.getUserId());
        UserListResp<UserConsumerDTO> result=new UserListResp<>();
        result.setPage(PageHelper.setResponsePage(userConsumerInfo));
        if (CollectionUtils.isEmpty(userConsumerInfo.getRecords())){
            result.setData(Collections.emptyList());
            return result;
        }
        userConsumerInfo.getRecords().forEach(i -> {
            i.setConsumerType(ConsumerType.getDisplayNameByOrdinal(Integer.valueOf(i.getConsumerType())));
            i.setNoticeUserType(NoticeUserType.getDisplayNameByOrdinal(Integer.valueOf(i.getNoticeUserType())));
        });
        result.setData(userConsumerInfo.getRecords());
        return result;
    }


}
