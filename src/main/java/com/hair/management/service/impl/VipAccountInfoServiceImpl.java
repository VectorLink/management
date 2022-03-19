package com.hair.management.service.impl;

import com.hair.management.bean.enumerate.ConsumerType;
import com.hair.management.bean.enumerate.NoticeUserType;
import com.hair.management.bean.param.ChargeAccountParam;
import com.hair.management.dao.entity.UserConsumerInfo;
import com.hair.management.dao.entity.VipAccountInfo;
import com.hair.management.dao.VipAccountInfoMapper;
import com.hair.management.dao.entity.VipUser;
import com.hair.management.service.UserConsumerInfoService;
import com.hair.management.service.VipAccountInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hair.management.service.VipUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 会员账户信息表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2022-03-19
 */
@Service
public class VipAccountInfoServiceImpl extends ServiceImpl<VipAccountInfoMapper, VipAccountInfo> implements VipAccountInfoService {
    @Resource
    private UserConsumerInfoService userConsumerInfoService;
    @Resource
    private VipUserService vipUserService;

    @Override
    public Boolean addVipAccountInfo(Long vipUserId) {
        Assert.notNull(vipUserId,"添加会员账户信息时，会员ID不能为空");
        return this.saveOrUpdate(
                VipAccountInfo.builder()
                        .updateTime(LocalDateTime.now())
                        .createTime(LocalDateTime.now())
                        .userId(vipUserId)
                        .build()
        );
    }

    @Override
    public String changeAccountByUserId(ChargeAccountParam param) {
        //获取对应的账户信息
        VipAccountInfo accountInfo = this.lambdaQuery().eq(VipAccountInfo::getUserId, param.getUserId()).one();
        Assert.notNull(accountInfo,"无法找到该会员的账户信息");
        VipUser vipUser=vipUserService.getById(param.getUserId());
        Assert.notNull(vipUser,"会员信息不存在");

        BigDecimal changeAmount=BigDecimal.valueOf(Double.parseDouble(param.getAlterAmount()));
        //todo 这个要换成 cookie里面获取对应的发艺师ID
        Long haiMasterId=0L;
        //消费金额
        BigDecimal afterAmount;
        //构建结果消息
        String result="";
        //日志消息
        UserConsumerInfo userConsumerInfo=null;
        if (param.getConsumerType().equals(ConsumerType.normal_consumer)){
            //验证一下金额是否够减
            if (accountInfo.getAccountAmount().compareTo(changeAmount)<0){
                throw new RuntimeException("账户余额少于消费金额，请先充值后在进行消费");
            }
            afterAmount=accountInfo.getAccountAmount().subtract(changeAmount);
            //构建消费日志， 记录消费日志
             userConsumerInfo=UserConsumerInfo.builder()
                    .consumerAmount(changeAmount)
                    .preAccountAmount(accountInfo.getAccountAmount())
                    .afterAccountAmount(afterAmount)
                    .consumerTime(LocalDateTime.now())
                    .consumerType(param.getConsumerType().ordinal())
                    .hair_master_id(haiMasterId)
                    .noticeUser(Boolean.FALSE)
                    .noticeUserType(NoticeUserType.MESSAGE.ordinal())
                    .vipUserId(param.getUserId())
                    .build();
            //构建成功消息
            result= String.format("会员：【%s】消费成功，本次消费金额：%f,消费前账户金额：%f,消费后账户金额：%f", vipUser.getUserName(),
                    changeAmount.setScale(2, RoundingMode.HALF_UP),
                    accountInfo.getAccountAmount().setScale(2, RoundingMode.HALF_UP),
                    afterAmount.setScale(2, RoundingMode.HALF_UP));
            accountInfo.setUpdateTime(LocalDateTime.now());
            accountInfo.setAccountAmount(afterAmount);

        }else if (param.getConsumerType().equals(ConsumerType.charge)){
            afterAmount=accountInfo.getAccountAmount().add(changeAmount);
             userConsumerInfo=UserConsumerInfo.builder()
                    .consumerAmount(changeAmount)
                    .preAccountAmount(accountInfo.getAccountAmount())
                    .afterAccountAmount(afterAmount)
                    .consumerTime(LocalDateTime.now())
                    .consumerType(param.getConsumerType().ordinal())
                    .hair_master_id(haiMasterId)
                    .noticeUser(Boolean.FALSE)
                    .noticeUserType(NoticeUserType.MESSAGE.ordinal())
                    .vipUserId(param.getUserId())
                    .build();
            result= String.format("会员：【%s】充值成功，本次充值：%f,消费前账户金额：%f,消费后账户金额：%f", vipUser.getUserName(),
                    changeAmount.setScale(2, RoundingMode.HALF_UP),
                    accountInfo.getAccountAmount().setScale(2, RoundingMode.HALF_UP),
                    afterAmount.setScale(2, RoundingMode.HALF_UP));
            accountInfo.setUpdateTime(LocalDateTime.now());
            accountInfo.setAccountAmount(afterAmount);
        }
        if (Objects.nonNull(userConsumerInfo)) {
            userConsumerInfoService.saveOrUpdate(userConsumerInfo);
            this.saveOrUpdate(accountInfo);
        }
        //todo 这里消费完成后，异步调用接口发送短信消息到会员手机里面(需要等事务提交后再发送）
        return result;
    }
}
