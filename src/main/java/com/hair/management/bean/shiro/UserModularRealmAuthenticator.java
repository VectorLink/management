package com.hair.management.bean.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

@Slf4j
public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("开始判断选择");
        assertRealmsConfigured();
        Collection<Realm> realms = getRealms();
        if (authenticationToken instanceof JwtToken) {
            for (Realm realm : realms) {
                if (realm.getName().contains("Jwt")) {
                    return doSingleRealmAuthentication(realm, authenticationToken);
                }
            }
        } else if (authenticationToken instanceof UsernamePasswordToken) {
            for (Realm realm : realms) {
                if (realm.getName().contains("login")) {
                    return doSingleRealmAuthentication(realm, authenticationToken);
                }
            }
        }
        return null;
    }
}
