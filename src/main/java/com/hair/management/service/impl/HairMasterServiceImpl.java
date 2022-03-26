package com.hair.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hair.management.bean.Constants;
import com.hair.management.bean.enumerate.HairMasterType;
import com.hair.management.bean.param.HairMasterParam;
import com.hair.management.bean.response.HairMasterDTO;
import com.hair.management.bean.shiro.HairMasterProfile;
import com.hair.management.dao.entity.HairMaster;
import com.hair.management.dao.HairMasterMapper;
import com.hair.management.service.HairMasterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 发型师表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2022-03-20
 */
@Service
public class HairMasterServiceImpl extends ServiceImpl<HairMasterMapper, HairMaster> implements HairMasterService {
    @Override
    public HairMaster getByCode(String code) {
        return this.lambdaQuery().eq(HairMaster::getHairMasterCode,code).one();
    }

    @Override
    public HairMaster getCurrentHairMaster() {
        HairMasterProfile hairMasterProfile = (HairMasterProfile) SecurityUtils.getSubject().getPrincipal();
        HairMaster byId = this.getById(hairMasterProfile.getHairMasterId());
        Assert.notNull(byId, "不存在的发型师");
        return byId;
    }

    @Override
    public List<HairMasterDTO> getAllHairMaster() {
        return this.lambdaQuery().eq(HairMaster::getStatus,1).list().stream()
                .map(l-> HairMasterDTO.builder()
                        .hairMasterId(l.getHairMasterId())
                        .hairMasterName(l.getHairMasterName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public IPage<HairMaster> listHairMasters(HairMasterParam param) {
        Page<HairMaster> page= new Page<>(param.getPage().getCurrent(), param.getPage().getSize());
        LambdaQueryWrapper<HairMaster> hairMasterLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(param.getHairMasterCode())){
            hairMasterLambdaQueryWrapper.like(HairMaster::getHairMasterCode,param.getHairMasterCode());
        }
        if (StringUtils.isNotBlank(param.getHairMasterName())){
            hairMasterLambdaQueryWrapper.like(HairMaster::getHairMasterName,param.getHairMasterName());
        }
        IPage<HairMaster> result = this.baseMapper.selectPage(page, hairMasterLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(result.getRecords())){
            return result;
        }
        //密码职位空
        result.getRecords().forEach(l->l.setPassword(null));
        return result;
    }

    @Override
    public Boolean saveOrUpdateMaster(HairMaster hairMaster) {
        //校验权限
        HairMaster currentHairMaster = this.getCurrentHairMaster();
        if (!currentHairMaster.getType().equals(HairMasterType.ADMIN.ordinal())){
            throw new RuntimeException("您不是管理员，无权进行发型师管理维护");
        }
        Optional.ofNullable(hairMaster.getPassword()).ifPresent(l->{
            Assert.isTrue(l.length()>=6,"密码长度必须大于6位");
        });
        HairMaster entity=HairMaster.builder().build();
        if (Objects.nonNull(hairMaster.getHairMasterId())){
            //验证信息是否重复
            HairMaster originalMaster = this.getById(hairMaster.getHairMasterId());
            if (!StringUtils.equals(hairMaster.getHairMasterCode(),originalMaster.getHairMasterCode())){
                HairMaster one = this.getHairMasterByCode(hairMaster.getHairMasterCode());
                Assert.isTrue(one.getHairMasterId().equals(hairMaster.getHairMasterId()),"用户登录名已经存在");
            }
            BeanUtils.copyProperties(hairMaster,entity);
        }else {
            HairMaster original = this.getHairMasterByCode(hairMaster.getHairMasterCode());
            Assert.isNull(original,"已存在登录名");
            BeanUtils.copyProperties(hairMaster,entity);
        }
        entity.setPassword(DigestUtils.md5DigestAsHex((Constants.MD5_KEY+hairMaster.getPassword()).getBytes(StandardCharsets.UTF_8)));
        return this.saveOrUpdate(entity);
    }

    @Override
    public HairMaster getHairMasterByCode(String hairMasterCode) {
        return this.lambdaQuery().eq(HairMaster::getHairMasterCode,hairMasterCode).one();
    }
}
