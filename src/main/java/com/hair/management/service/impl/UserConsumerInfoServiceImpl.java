package com.hair.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hair.management.bean.enumerate.ConsumerType;
import com.hair.management.bean.enumerate.NoticeUserType;
import com.hair.management.bean.param.StatisticParam;
import com.hair.management.bean.param.UserConsumerParam;
import com.hair.management.bean.response.AllUserStatisticDto;
import com.hair.management.bean.response.UserConsumerDTO;
import com.hair.management.bean.response.UserListResp;
import com.hair.management.bean.response.UserStatisticDTO;
import com.hair.management.dao.entity.HairMaster;
import com.hair.management.dao.entity.UserConsumerInfo;
import com.hair.management.dao.UserConsumerInfoMapper;
import com.hair.management.service.HairMasterService;
import com.hair.management.service.UserConsumerInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hair.management.util.LocalDateTimeUtil;
import com.hair.management.util.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    @Resource
    private HairMasterService hairMasterService;
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

    @Override
    public UserListResp<UserConsumerDTO> getMasterBillByMasterId(UserConsumerParam param) {
        Page page=new Page(param.getPage().getCurrent(),param.getPage().getSize());
        page.addOrder(OrderItem.desc("uci.consumer_time"));
        IPage<UserConsumerDTO> userConsumerInfo = this.baseMapper.getMasterBillByMasterId(page, param.getUserId());
        UserListResp<UserConsumerDTO> result=new UserListResp<>();
        result.setPage(PageHelper.setResponsePage(userConsumerInfo));
        if (CollectionUtils.isEmpty(userConsumerInfo.getRecords())){
            result.setData(Collections.emptyList());
            return result;
        }
        userConsumerInfo.getRecords().forEach(i -> {
            i.setConsumerType(ConsumerType.getDisplayNameByOrdinal(Integer.valueOf(i.getConsumerType())));
            i.setNoticeUserType(NoticeUserType.getDisplayNameByOrdinal(Integer.valueOf(i.getNoticeUserType())));
            i.setVipUserName(i.getVipUserId().equals(0L)?"非会员": i.getVipUserName());
        });
        result.setData(userConsumerInfo.getRecords());
        return result;
    }

    @Override
    public UserStatisticDTO getHairMasterStatistic(StatisticParam param) {
        HairMaster currentHairMaster = hairMasterService.getCurrentHairMaster();
        LambdaQueryWrapper<UserConsumerInfo> queryWrapper = this.getUserConsumerDataQueryWrapper(param);
        queryWrapper.eq(UserConsumerInfo::getHairMasterId,currentHairMaster.getHairMasterId());
        List<UserConsumerInfo> list =this.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)){
            return UserStatisticDTO.builder()
                    .chargeAmount(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                    .consumerAmount(BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP))
                    .userTotal(0)
                    .build();
        }
        double consumerAmount = list.stream().filter(l -> l.getConsumerType().equals(ConsumerType.normal_consumer.ordinal()))
                .mapToDouble(l -> l.getConsumerAmount().doubleValue()).sum();
        double chargeAmount = list.stream().filter(l -> l.getConsumerType().equals(ConsumerType.charge.ordinal()))
                .mapToDouble(l -> l.getConsumerAmount().doubleValue()).sum();
        long userCount = list.stream().map(UserConsumerInfo::getVipUserId).distinct().count();
        return UserStatisticDTO.
                builder()
                .consumerAmount(BigDecimal.valueOf(consumerAmount).setScale(2,RoundingMode.HALF_UP))
                .chargeAmount(BigDecimal.valueOf(chargeAmount).setScale(2,RoundingMode.HALF_UP))
                .userTotal((int) userCount)
                .build();
    }

    @Override
    public List<AllUserStatisticDto> getAllStatistic(StatisticParam param) {
        LambdaQueryWrapper<UserConsumerInfo> queryWrapper = getUserConsumerDataQueryWrapper(param);
        List<UserConsumerInfo> list = this.list(queryWrapper);
        //按人分组
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        Map<Long, List<UserConsumerInfo>> masterGroup = list.stream().collect(Collectors.groupingBy(UserConsumerInfo::getHairMasterId));
        //获取对应的hairMaster名称
        Map<Long, String> hairMasterMap = hairMasterService.listByIds(masterGroup.keySet()).stream()
                .collect(Collectors.toMap(HairMaster::getHairMasterId, HairMaster::getHairMasterName));
        List<AllUserStatisticDto> result = new ArrayList<>(hairMasterMap.size());
        //瓶装数据
        masterGroup.forEach((k, v) -> {
            result.add(
                    this.buildAllUserStatisticModel(Optional.ofNullable(hairMasterMap.get(k)).orElse("未知发型师"), v)
            );
        });
        return result;
    }

    private AllUserStatisticDto buildAllUserStatisticModel(String hairMasterName, List<UserConsumerInfo> list) {
        List<UserConsumerInfo> notVipConsumer = list.stream().filter(l -> l.getVipUserId().equals(0L)).collect(Collectors.toList());
        List<UserConsumerInfo> vipConsumer = list.stream().filter(l -> !l.getVipUserId().equals(0L)).collect(Collectors.toList());

        //会员消费
        double vipConsumerAmount = this.getSum(vipConsumer, l -> l.getConsumerType().equals(ConsumerType.normal_consumer.ordinal()));
        //会员充值
        double vipChargeAmount = this.getSum(vipConsumer, l -> l.getConsumerType().equals(ConsumerType.charge.ordinal()));
        //非会员消费
        double notVipConsumerAmount = this.getSum(notVipConsumer, l -> l.getConsumerType().equals(ConsumerType.normal_consumer.ordinal()));
        //会员数量
        int vipConsumerTotal = this.getTotal(vipConsumer);
        //非会员消费笔数
        int notVipConsumerTotal = notVipConsumer.size();
        return AllUserStatisticDto.builder()
                .vipStatistic(
                        UserStatisticDTO.builder()
                                .userTotal(vipConsumerTotal)
                                .chargeAmount(BigDecimal.valueOf(vipChargeAmount).setScale(2, RoundingMode.HALF_UP))
                                .consumerAmount(BigDecimal.valueOf(vipConsumerAmount).setScale(2, RoundingMode.HALF_UP))
                                .build())
                .notVipStatistic(
                        UserStatisticDTO.builder()
                                .consumerAmount(BigDecimal.valueOf(notVipConsumerAmount).setScale(2, RoundingMode.HALF_UP))
                                .chargeAmount(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                                .userTotal(notVipConsumerTotal)
                                .build())
                .hairMasterName(hairMasterName)
                .build();
    }

    /**
     * 求和
     * @param list
     * @param predicate
     * @return
     */
    private double getSum(List<UserConsumerInfo> list, Predicate<UserConsumerInfo> predicate){
        return list.stream().filter(predicate).mapToDouble(l -> l.getConsumerAmount().doubleValue()).sum();
    }

    /**
     * 计数
     * @param list
     * @return
     */
    private int getTotal(List<UserConsumerInfo> list){
        return (int) list.stream().map(UserConsumerInfo::getVipUserId).distinct().count();
    }

    private LambdaQueryWrapper<UserConsumerInfo> getUserConsumerDataQueryWrapper(StatisticParam param) {
        LambdaQueryWrapper<UserConsumerInfo> queryWrapper= Wrappers.lambdaQuery(UserConsumerInfo.class);
        //只获取登录人营业信息
        if (Objects.nonNull(param)&&Objects.nonNull(param.getStart())){
            queryWrapper.ge(UserConsumerInfo::getConsumerTime, LocalDateTimeUtil.getDayStart(param.getStart()));
        }
        if (Objects.nonNull(param)&&Objects.nonNull(param.getEnd())){
            queryWrapper.le(UserConsumerInfo::getConsumerTime, LocalDateTimeUtil.getDayEnd(param.getEnd()));
        }
        if (Objects.isNull(param)||Objects.isNull(param.getStart())||Objects.isNull(param.getEnd())){
            queryWrapper.ge(UserConsumerInfo::getConsumerTime,LocalDate.now().atStartOfDay());
        }
        return queryWrapper;
    }
}
