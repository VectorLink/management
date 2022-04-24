package com.hair.management.bean.shiro;

import com.hair.management.dao.entity.HairMaster;
import com.hair.management.service.HairMasterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
public class LoginRealm extends AuthorizingRealm {
    @Resource
    HairMasterService hairMasterService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("登录认证开始，传入token{}",authenticationToken);
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        log.info("token 转变为：{}",token);
        HairMaster hairMasterCode = hairMasterService.getByCode(token.getUsername());
        if (Objects.isNull(hairMasterCode)){
            throw new UnknownAccountException("不存在的账号");
        }
        Object credentials = token.getCredentials();
        return new SimpleAuthenticationInfo(hairMasterCode,credentials,
                ByteSource.Util.bytes(hairMasterCode.getHairMasterId()),getName());
    }
}
