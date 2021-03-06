package com.hair.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.hair.management.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
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
@Slf4j
public class HairMasterServiceImpl extends ServiceImpl<HairMasterMapper, HairMaster> implements HairMasterService {
    @Override
    public HairMaster getByCode(String code) {
        return this.lambdaQuery().eq(HairMaster::getHairMasterCode,code).eq(HairMaster::getStatus,1).one();
    }

    @Override
    public HairMaster getCurrentHairMaster() {
        Long hairMasterId = JwtUtils.getHairMasterIdByToken(SecurityUtils.getSubject().getPrincipal().toString());
        HairMaster byId = this.getById(hairMasterId);
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
        if (StringUtils.isNotBlank(param.getSearchParam())){
            hairMasterLambdaQueryWrapper.like(HairMaster::getHairMasterCode,param.getSearchParam()).or()
                    .like(HairMaster::getHairMasterName,param.getSearchParam());
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
        if (!currentHairMaster.getType().equals(HairMasterType.OWNER.ordinal())){
            throw new RuntimeException("您不是店长，无权进行发型师管理维护");
        }
        HairMaster entity=HairMaster.builder().build();
        if (Objects.nonNull(hairMaster.getHairMasterId())){
            //验证信息是否重复
            HairMaster originalMaster = this.getById(hairMaster.getHairMasterId());
            if (!StringUtils.equals(hairMaster.getHairMasterCode(),originalMaster.getHairMasterCode())){
                HairMaster one = this.getHairMasterByCode(hairMaster.getHairMasterCode());
                Assert.isTrue(Objects.nonNull(one)&&hairMaster.getHairMasterId().equals(one.getHairMasterId()),"用户登录名已经存在");
            }
            BeanUtils.copyProperties(hairMaster,entity);

        }else {
            HairMaster original = this.getHairMasterByCode(hairMaster.getHairMasterCode());
            Assert.isNull(original,"已存在登录名");
            BeanUtils.copyProperties(hairMaster,entity);
        }
        entity.setPassword(DigestUtils.md5DigestAsHex((Constants.MD5_KEY+hairMaster.getPassword()).getBytes(StandardCharsets.UTF_8)));
        log.info("时间："+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return this.saveOrUpdate(entity);
    }

    @Override
    public HairMaster getHairMasterByCode(String hairMasterCode) {
        return this.lambdaQuery().eq(HairMaster::getHairMasterCode,hairMasterCode).one();
    }
}
