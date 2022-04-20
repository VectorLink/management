package com.hair.management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hair.management.bean.Constants;
import com.hair.management.bean.param.HairMasterParam;
import com.hair.management.bean.param.LoginParam;
import com.hair.management.bean.param.StatisticParam;
import com.hair.management.bean.response.*;
import com.hair.management.bean.shiro.HairMasterProfile;
import com.hair.management.dao.entity.HairMaster;
import com.hair.management.service.HairMasterService;
import com.hair.management.service.UserConsumerInfoService;
import com.hair.management.util.JwtUtils;
import com.hair.management.util.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
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
    private UserConsumerInfoService userConsumerInfoService;

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    @ApiOperation("登录")
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
        String s = JwtUtils.generateToken(byCode.getHairMasterId());
        response.setHeader(Constants.AUTHORIZATION,s);
        response.setHeader("Access-Control-Expose-Headers",Constants.AUTHORIZATION);
        HairMasterProfile profile=new HairMasterProfile();
        BeanUtils.copyProperties(byCode,profile);
        return ApiResult.success(profile);
    }
    @ApiOperation("登出")
    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    @RequiresAuthentication
    public ApiResult<String> logout(){
        SecurityUtils.getSubject().logout();
        return ApiResult.success();
    }
    @PostMapping("/getHairMaster")
    @ApiOperation("获取发型师")
    public ApiResult<List<HairMasterDTO>> getHairMaster(){
        return ApiResult.success(hairMasterService.getAllHairMaster());
    }

    @PostMapping("/listHairMaster")
    @ApiOperation("获取发型师列表")
    public ApiResult<HairMasterResp> listHairMaster(@RequestBody HairMasterParam param) {
        IPage<HairMaster> hairMasterIPage = hairMasterService.listHairMasters(param);
        return ApiResult.success(HairMasterResp.builder()
                .data(hairMasterIPage.getRecords())
                .page(PageHelper.setResponsePage(hairMasterIPage))
                .build());
    }
    @PostMapping("/saveOrderUpdateMaster")
    @ApiOperation("保存或者增加发型师")
    public ApiResult<Boolean> saveOrUpdateMaster(@RequestBody HairMaster hairMaster){
        return ApiResult.success(hairMasterService.saveOrUpdateMaster(hairMaster));
    }
    @PostMapping("/getStatistic")
    @ApiOperation("获取发型师个人统计信息")
    public ApiResult<UserStatisticDTO> getHairMasterStatistic(@RequestBody StatisticParam param){
        return ApiResult.success(userConsumerInfoService.getHairMasterStatistic(param));
    }

    @PostMapping("/getAllStatistic")
    @ApiOperation("获取所有发型师统计信息")
    public ApiResult<List<AllUserStatisticDto>> getAllStatistic(@RequestBody StatisticParam param){
        return ApiResult.success(userConsumerInfoService.getAllStatistic(param));
    }

}
