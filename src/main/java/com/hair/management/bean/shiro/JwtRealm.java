package com.hair.management.bean.shiro;

import com.hair.management.bean.enumerate.HairMasterType;
import com.hair.management.dao.entity.HairMaster;
import com.hair.management.service.HairMasterService;
import com.hair.management.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.Objects;


@Slf4j
public class JwtRealm extends AuthorizingRealm {
    @Resource
    private HairMasterService hairMasterService;
    @Override
    public boolean supports(AuthenticationToken token) {
        return  token instanceof JwtToken;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String token=principalCollection.toString();
        Long hairMasterId = JwtUtils.getHairMasterIdByToken(token);
        HairMaster hairMaster = hairMasterService.getById(hairMasterId);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if (hairMaster.getType().equals(HairMasterType.OWNER.ordinal())){
            simpleAuthorizationInfo.addRole(HairMasterType.OWNER.name());
            simpleAuthorizationInfo.addStringPermission(HairMasterType.OWNER.getDisplayName());
        }else {
            simpleAuthorizationInfo.addRole(HairMasterType.HAIR_MASTER.name());
            simpleAuthorizationInfo.addStringPermission(HairMasterType.HAIR_MASTER.getDisplayName());
        }



        return simpleAuthorizationInfo;
    }

    /**
     * 认证.登录
     * doGetAuthenticationInfo这个方法是在用户登录的时候调用的
     * 也就是执行SecurityUtils.getSubject().login()的时候调用；(即:登录验证)
     * 验证通过后会用户保存在缓存中的
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        Long hairMasterId = JwtUtils.getHairMasterIdByToken(token);
        HairMaster byId = hairMasterService.getById(hairMasterId);
        if (Objects.isNull(byId)){
            throw new AuthenticationException("账户不存在");
        }
        if (byId.getStatus()!=1){
            throw new AuthenticationException("用户不可用");
        }
        if (!JwtUtils.verify(token)){
            throw new AuthenticationException("token 失效，请重新登录");
        }
        return new SimpleAuthenticationInfo(token,token,getName());
    }
}
