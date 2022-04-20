package com.hair.management.bean.shiro;

import com.hair.management.dao.entity.HairMaster;
import com.hair.management.service.HairMasterService;
import com.hair.management.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
@Slf4j
public class HairMasterRealm extends AuthorizingRealm {
    @Resource
    private HairMasterService hairMasterService;
    @Override
    public boolean supports(AuthenticationToken token) {
        return  token instanceof JwtToken;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (Objects.isNull( authenticationToken.getPrincipal())){
            return null;
        }
        JwtToken jwtToken= (JwtToken) authenticationToken;
        String hairMasterCode = JwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        HairMaster byId = hairMasterService.getById(Long.valueOf(hairMasterCode));
        if (Objects.isNull(byId)){
           return null;
        }
        if (byId.getStatus()!=1){
            log.error("用户不可用");
           return null;
        }
        HairMasterProfile profile=new HairMasterProfile();
        BeanUtils.copyProperties(byId,profile);
        return new SimpleAuthenticationInfo(profile,jwtToken.getPrincipal(),getName());
    }
}
