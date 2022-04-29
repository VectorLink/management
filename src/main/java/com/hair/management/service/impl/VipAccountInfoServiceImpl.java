package com.hair.management.service.impl;

import com.hair.management.bean.enumerate.ConsumerType;
import com.hair.management.bean.enumerate.HairMasterType;
import com.hair.management.bean.enumerate.NoticeUserType;
import com.hair.management.bean.param.ChargeAccountParam;
import com.hair.management.dao.entity.HairMaster;
import com.hair.management.dao.entity.UserConsumerInfo;
import com.hair.management.dao.entity.VipAccountInfo;
import com.hair.management.dao.VipAccountInfoMapper;
import com.hair.management.dao.entity.VipUser;
import com.hair.management.service.HairMasterService;
import com.hair.management.service.UserConsumerInfoService;
import com.hair.management.service.VipAccountInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hair.management.service.VipUserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
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
    @Lazy
    private VipUserService vipUserService;
    @Resource
    private HairMasterService hairMasterService;

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
    public String changeAccountByUserId(ChargeAccountParam param, MultipartFile signImg)  {
        //获取对应的账户信息
        VipAccountInfo accountInfo = this.lambdaQuery().eq(VipAccountInfo::getUserId, param.getUserId()).one();
        Assert.notNull(accountInfo,"无法找到该会员的账户信息");
        VipUser vipUser=vipUserService.getById(param.getUserId());
        Assert.notNull(vipUser,"会员信息不存在");

        BigDecimal changeAmount=param.getAlterAmount();
        Long haiMasterId=hairMasterService.getCurrentHairMaster().getHairMasterId();
        //消费金额
        BigDecimal afterAmount;
        //构建结果消息
        String result="";
        //日志消息
        UserConsumerInfo userConsumerInfo=null;
        ConsumerType consumerType = ConsumerType.getByOrdinal(param.getConsumerType());
        if(consumerType ==null ){
            throw new RuntimeException("消费类型无效");
        }
        if (consumerType.equals(ConsumerType.normal_consumer)){
            //必须要有签名
            Assert.notNull(signImg,"签名不存在，请检查是否已签名");
            //验证一下金额是否够减
            if (accountInfo.getAccountAmount().compareTo(changeAmount)<0){
                throw new RuntimeException("账户余额少于消费金额，请先充值后在进行消费");
            }
            afterAmount=accountInfo.getAccountAmount().subtract(changeAmount);
            try {
                byte[] imgBytes=signImg.getBytes();
                //构建消费日志， 记录消费日志
                userConsumerInfo = UserConsumerInfo.builder()
                        .consumerAmount(changeAmount)
                        .preAccountAmount(accountInfo.getAccountAmount())
                        .afterAccountAmount(afterAmount)
                        .consumerTime(LocalDateTime.now())
                        .consumerType(param.getConsumerType())
                        .hairMasterId(haiMasterId)
                        .noticeUser(Boolean.FALSE)
                        .noticeUserType(NoticeUserType.MESSAGE.ordinal())
                        .vipUserId(param.getUserId())
                        .signImg(imgBytes)
                        .build();
            } catch (IOException e) {
                log.error("图片解码错误:",e);
              throw new RuntimeException("图片解码错误");
            }

            //构建成功消息
            result= String.format("会员：【%s】消费成功，本次消费金额：%s,消费前账户金额：%s,消费后账户金额：%s", vipUser.getUserName(),
                    changeAmount.setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    accountInfo.getAccountAmount().setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    afterAmount.setScale(2, RoundingMode.HALF_UP).toPlainString());
            accountInfo.setUpdateTime(LocalDateTime.now());
            accountInfo.setAccountAmount(afterAmount);

        }else if (consumerType.equals(ConsumerType.charge)){
            afterAmount=accountInfo.getAccountAmount().add(changeAmount);
             userConsumerInfo=UserConsumerInfo.builder()
                    .consumerAmount(changeAmount)
                    .preAccountAmount(accountInfo.getAccountAmount())
                    .afterAccountAmount(afterAmount)
                    .consumerTime(LocalDateTime.now())
                    .consumerType(param.getConsumerType())
                    .hairMasterId(haiMasterId)
                    .noticeUser(Boolean.FALSE)
                    .noticeUserType(NoticeUserType.MESSAGE.ordinal())
                    .vipUserId(param.getUserId())
                    .build();
            result= String.format("会员：【%s】充值成功，本次充值：%s,消费前账户金额：%s,消费后账户金额：%s", vipUser.getUserName(),
                    changeAmount.setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    accountInfo.getAccountAmount().setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    afterAmount.setScale(2, RoundingMode.HALF_UP).toPlainString());
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

    @Override
    public String addNotVipBill(ChargeAccountParam param) {
        //当前用户
        HairMaster currentHairMaster = hairMasterService.getCurrentHairMaster();
        UserConsumerInfo userConsumerInfo= UserConsumerInfo.builder()
                .vipUserId(0L)
                .noticeUserType(NoticeUserType.NO_VIP_NOT_NOTICE.ordinal())
                .consumerAmount(param.getAlterAmount())
                .noticeUser(Boolean.TRUE)
                .hairMasterId(currentHairMaster.getHairMasterId())
                .consumerType(ConsumerType.normal_consumer.ordinal())
                .preAccountAmount(BigDecimal.ZERO)
                .afterAccountAmount(BigDecimal.ZERO)
                .consumerTime(LocalDateTime.now())
                .build();
        this.userConsumerInfoService.saveOrUpdate(userConsumerInfo);
        return String.format("%S操作成功，非会员消费金额：%s",currentHairMaster.getHairMasterName(),param.getAlterAmount().setScale(2,RoundingMode.HALF_UP).toPlainString());
    }
}
