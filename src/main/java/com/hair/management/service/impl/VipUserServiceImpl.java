package com.hair.management.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hair.management.bean.enumerate.SexEnum;
import com.hair.management.bean.param.SaveOrUpdateUserParam;
import com.hair.management.bean.param.VipUserQueryParam;
import com.hair.management.bean.response.VipUserListDTO;
import com.hair.management.bean.response.UserListResp;
import com.hair.management.dao.entity.VipUser;
import com.hair.management.dao.VipUserMapper;
import com.hair.management.service.HairMasterService;
import com.hair.management.service.VipAccountInfoService;
import com.hair.management.service.VipUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hair.management.util.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2022-03-19
 */
@Service
public class VipUserServiceImpl extends ServiceImpl<VipUserMapper, VipUser> implements VipUserService {
    @Resource
    private HairMasterService hairMasterService;
    @Autowired
    private VipAccountInfoService vipAccountInfoService;

    @Override
    public Boolean saveOrUpdateVipUser(SaveOrUpdateUserParam param) {
        //验证一下发艺师是否存在
        if (Objects.nonNull(param.getHairMasterId())) {
            Optional.ofNullable(hairMasterService.getById(param.getHairMasterId())).orElseThrow(() -> new RuntimeException("不存在的发艺师"));
        }
        Boolean telRegistered=this.isTelNumRegistered(param.getTelephone());
        if (Objects.nonNull(param.getUserId())){
            VipUser vipUser = this.getById(param.getUserId());
            Assert.notNull(vipUser,"会员用户不存在");
            if (telRegistered&&!StringUtils.equals(param.getTelephone(),vipUser.getTelephone())){
                throw new  RuntimeException(String.format("电话号码：%s已经被注册了！",param.getTelephone()));
            }
            vipUser.setUserName(param.getUserName());
            vipUser.setSex(param.getSex());
            vipUser.setHairMasterId(param.getHairMasterId());
            vipUser.setTelephone(param.getTelephone());
            vipUser.setUpdateTime(LocalDateTime.now());
           return this.saveOrUpdate(vipUser);

        }
        if (telRegistered){
            throw new  RuntimeException(String.format("电话号码：%s已经被注册了！",param.getTelephone()));
        }
        //如果新增用户，那么需要同时新增账户信息
        VipUser vipUser = new VipUser();
        BeanUtils.copyProperties(param, vipUser);
        vipUser.setCreateTime(LocalDateTime.now());
        vipUser.setUpdateTime(LocalDateTime.now());
        this.saveOrUpdate(vipUser);
        Boolean accountSuccess = vipAccountInfoService.addVipAccountInfo(vipUser.getUserId());
        if(!accountSuccess){
            throw new RuntimeException("账户添加失败");
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean isTelNumRegistered(String tel) {
        return Objects.nonNull(this.lambdaQuery().eq(VipUser::getTelephone, tel).one());
    }

    @Override
    public UserListResp<VipUserListDTO> listVipUserList(VipUserQueryParam param) {
        //构建分页参数
        Page page=new Page<>(param.getPage().getCurrent(),param.getPage().getSize());
        page.addOrder(OrderItem.asc("create_time"));
        IPage<VipUserListDTO> iPage = this.baseMapper.listVipUserList(page, param);
        UserListResp<VipUserListDTO> result=new UserListResp<VipUserListDTO>();
        result.setPage(PageHelper.setResponsePage(iPage));
        if (CollectionUtils.isEmpty(iPage.getRecords())){
            result.setData(Collections.emptyList());
            return result;
        }
        result.setData(iPage.getRecords());
        return result;
    }
}
