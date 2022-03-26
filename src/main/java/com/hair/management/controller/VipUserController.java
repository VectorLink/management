package com.hair.management.controller;

import com.hair.management.bean.param.ChargeAccountParam;
import com.hair.management.bean.param.SaveOrUpdateUserParam;
import com.hair.management.bean.param.UserConsumerParam;
import com.hair.management.bean.param.VipUserQueryParam;
import com.hair.management.bean.response.ApiResult;
import com.hair.management.bean.response.UserConsumerDTO;
import com.hair.management.bean.response.VipUserListDTO;
import com.hair.management.bean.response.UserListResp;
import com.hair.management.service.UserConsumerInfoService;
import com.hair.management.service.VipAccountInfoService;
import com.hair.management.service.VipUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;


@RequestMapping(value = "vipUser", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "会员管理")
@RestController
public class VipUserController {
    @Resource
    private VipUserService vipUserService;
    @Resource
    private UserConsumerInfoService userConsumerInfoService;
    @Resource
    private VipAccountInfoService vipAccountInfoService;

    @ApiOperation("添加会员")
    @RequestMapping(path = "addUser", method = RequestMethod.POST)
    public ApiResult<Boolean> addVipUser(@RequestBody SaveOrUpdateUserParam param) {
        Assert.isTrue(ObjectUtils.allNotNull(param, param.getUserName(), param.getTelephone(), param.getSex()),
                "修改和新增会员时，用户名，性别，联系电话不能为空");
        return ApiResult.success(vipUserService.saveOrUpdateVipUser(param));
    }

    @ApiOperation("分页查询会员")
    @RequestMapping(path = "listUser", method = RequestMethod.POST)
    public ApiResult<UserListResp<VipUserListDTO>> listVipUser(@RequestBody VipUserQueryParam param) {
        Assert.notNull(param, "参数不能为空");
        return ApiResult.success(vipUserService.listVipUserList(param));
    }

    @ApiOperation("分页查询会员消费历史")
    @RequestMapping(path = "listUserConsumer", method = RequestMethod.POST)
    public ApiResult<UserListResp<UserConsumerDTO>> listUserConsumerInfo(@RequestBody UserConsumerParam param) {
        Assert.isTrue(ObjectUtils.allNotNull(param, param.getUserId()), "用户ID不能为空");
        return ApiResult.success(userConsumerInfoService.getUserConsumerInfoByUserId(param));
    }
    @ApiOperation("会员充值或消费")
    @RequestMapping(path = "changeAccount",method = RequestMethod.POST)
    public ApiResult<String> changeAccountByUserId(@RequestBody ChargeAccountParam param){
        Assert.isTrue(ObjectUtils.allNotNull(param,param.getAlterAmount(),param.getConsumerType(),param.getUserId()),"充值或消费时，参数不能为空");
        Assert.isTrue(param.getAlterAmount().compareTo(BigDecimal.ZERO)>0,"请输入大于1的金额");
        return ApiResult.success(vipAccountInfoService.changeAccountByUserId(param));
    }

}
