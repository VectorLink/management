package com.hair.management.controller;

import com.hair.management.bean.enumerate.ChangeConsumerBillParam;
import com.hair.management.bean.param.ChargeAccountParam;
import com.hair.management.bean.param.SaveOrUpdateUserParam;
import com.hair.management.bean.param.UserConsumerParam;
import com.hair.management.bean.param.VipUserQueryParam;
import com.hair.management.bean.response.ApiResult;
import com.hair.management.bean.response.UserConsumerDTO;
import com.hair.management.bean.response.VipUserListDTO;
import com.hair.management.bean.response.UserListResp;
import com.hair.management.dao.entity.UserConsumerInfo;
import com.hair.management.service.UserConsumerInfoService;
import com.hair.management.service.VipAccountInfoService;
import com.hair.management.service.VipUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.Map;


@RequestMapping(value = "vipUser",produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "会员管理")
@RestController
public class VipUserController {
    @Resource
    private VipUserService vipUserService;
    @Resource
    private UserConsumerInfoService userConsumerInfoService;
    @Resource
    private VipAccountInfoService vipAccountInfoService;

    @ApiOperation("添加会员或修改会员")
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
    @ApiOperation("获取会员消费签名图片")
    @GetMapping(value = "getConsumerImg",produces = MediaType.IMAGE_PNG_VALUE)
    public BufferedImage getConsumerSign(Long consumerId) throws IOException {
        UserConsumerInfo id = userConsumerInfoService.getById(consumerId);
        return ImageIO.read(new ByteArrayInputStream(id.getSignImg()));
    }

    @ApiOperation("分页查询发型师账目")
    @RequestMapping(path = "listMasterBill", method = RequestMethod.POST)
    public ApiResult<UserListResp<UserConsumerDTO>> listMasterBill(@RequestBody UserConsumerParam param) {
        Assert.isTrue(ObjectUtils.allNotNull(param, param.getUserId()), "用户ID不能为空");
        return ApiResult.success(userConsumerInfoService.getMasterBillByMasterId(param));
    }
    @ApiOperation("修改账目金额")
    @PostMapping("changeBillAmount")
    @ResponseBody
    public ApiResult<String> changeBillAmount(@RequestBody ChangeConsumerBillParam param){
        Assert.isTrue(ObjectUtils.allNotNull(param,param.getChangeAmount(),param.getConsumerId()),"修改账目信息时，参数不能为空");
        userConsumerInfoService.changeConsumerBill(param);
        return ApiResult.success("操作成功");
    }

    @ApiOperation("会员消费")
    @RequestMapping(path = "consumerAccount",method = RequestMethod.POST)
    @ResponseBody
    public ApiResult<String> changeAccountByUserId(@RequestParam(value = "consumerType") Integer consumerType,
                                                   @RequestParam(value = "alterAmount") BigDecimal alterAmount,
                                                   @RequestParam(value = "userId") Long userId,
                                                   @RequestParam(value = "sign") MultipartFile sign) {
        Assert.isTrue(ObjectUtils.allNotNull(consumerType,alterAmount,userId,sign),"充值或消费时，参数不能为空");
        ChargeAccountParam param=ChargeAccountParam.builder()
                .consumerType(consumerType)
                .alterAmount(alterAmount)
                .userId(userId)
                .build();
        Assert.isTrue(param.getAlterAmount().compareTo(BigDecimal.ZERO)>0,"请输入大于0的金额");
        return ApiResult.success(vipAccountInfoService.changeAccountByUserId(param,sign));

    }

    @ApiOperation("会员充值")
    @PostMapping(path = "chargeAccount")
    public ApiResult<String> consumerAccountByUserId(@RequestBody ChargeAccountParam param) {
        Assert.isTrue(ObjectUtils.allNotNull(param, param.getConsumerType(), param.getUserId(), param.getAlterAmount()), "参数不能为空");
        return ApiResult.success(vipAccountInfoService.changeAccountByUserId(param, null));
    }

    @ApiOperation("非会员入账")
    @RequestMapping(path = "addBill",method = RequestMethod.POST)
    public ApiResult<String> addNotVipUserBill(@RequestBody ChargeAccountParam param){
        Assert.isTrue(param.getAlterAmount().compareTo(BigDecimal.ZERO)>0,"请输入大于0的金额");
        return ApiResult.success(vipAccountInfoService.addNotVipBill(param));
    }


}
