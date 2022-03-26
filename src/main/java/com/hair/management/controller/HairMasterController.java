package com.hair.management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hair.management.bean.Constants;
import com.hair.management.bean.param.HairMasterParam;
import com.hair.management.bean.param.LoginParam;
import com.hair.management.bean.response.ApiResult;
import com.hair.management.bean.response.HairMasterDTO;
import com.hair.management.bean.response.HairMasterResp;
import com.hair.management.bean.shiro.HairMasterProfile;
import com.hair.management.dao.entity.HairMaster;
import com.hair.management.service.HairMasterService;
import com.hair.management.util.JwtUtils;
import com.hair.management.util.PageHelper;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@RestController
@Api(tags = "发型师管理")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class HairMasterController {
    @Resource
    private HairMasterService hairMasterService;
    @Resource
    private JwtUtils jwtUtils;

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public ApiResult<Object> login(@RequestBody LoginParam param, HttpServletResponse response){
        HairMaster byCode = hairMasterService.getByCode(param.getUsername());
        if (Objects.isNull(byCode)){
            return ApiResult.error("用户不存在");
        }
        if (!StringUtils.equals(byCode.getPassword(),
                DigestUtils.md5DigestAsHex((Constants.MD5_KEY + param.getPassword())
                        .getBytes(StandardCharsets.UTF_8)))) {
            return ApiResult.error("密码不正确");
        }
        String s = jwtUtils.generateToken(byCode.getHairMasterId());
        response.setHeader(Constants.AUTHORIZATION,s);
        response.setHeader("Access-Control-Expose-Headers",Constants.AUTHORIZATION);
        HairMasterProfile profile=new HairMasterProfile();
        BeanUtils.copyProperties(byCode,profile);
        return ApiResult.success(profile);
    }
    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    @RequiresAuthentication
    public ApiResult<String> logout(){
        SecurityUtils.getSubject().logout();
        return ApiResult.success();
    }
    @PostMapping("/getHairMaster")
    public ApiResult<List<HairMasterDTO>> getHairMaster(){
        return ApiResult.success(hairMasterService.getAllHairMaster());
    }

    @PostMapping("/listHairMaster")
    public ApiResult<HairMasterResp> listHairMaster(@RequestBody HairMasterParam param) {
        IPage<HairMaster> hairMasterIPage = hairMasterService.listHairMasters(param);
        return ApiResult.success(HairMasterResp.builder()
                .data(hairMasterIPage.getRecords())
                .page(PageHelper.setResponsePage(hairMasterIPage))
                .build());
    }
    @PostMapping("/saveOrderUpdateMaster")
    public ApiResult<Boolean> saveOrUpdateMaster(@RequestBody HairMaster hairMaster){
        return ApiResult.success(hairMasterService.saveOrUpdateMaster(hairMaster));
    }

}
