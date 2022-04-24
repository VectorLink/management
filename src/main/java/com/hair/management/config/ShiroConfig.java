package com.hair.management.config;


import com.hair.management.bean.shiro.JwtRealm;
import com.hair.management.bean.shiro.JwtFilter;
import com.hair.management.bean.shiro.LoginRealm;
import com.hair.management.bean.shiro.UserModularRealmAuthenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/");
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        Map<String, String> filterChainMap = new LinkedHashMap<>();
        filterChainMap.put("/swagger**/**", "anon");
        filterChainMap.put("/webjars/**", "anon");
        filterChainMap.put("/v2/**", "anon");
        filterChainMap.put("/login","anon");
        filterChainMap.put("/**", "jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        Map<String, Filter> filterMap=new LinkedHashMap<>();
        filterMap.put("jwt",new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        return shiroFilterFactoryBean;
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    @Bean
    public SecurityManager securityManager(LoginRealm loginRealm,JwtRealm jwtRealm,
                                           UserModularRealmAuthenticator userModularRealmAuthenticator){
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        securityManager.setAuthenticator(userModularRealmAuthenticator);
        securityManager.setRealms(Arrays.asList(loginRealm,jwtRealm));

        return securityManager;
    }

    /**
     * 设置加密规则
     * @return
     */
    @Bean("matcher")
    public HashedCredentialsMatcher matcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(1024);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }
    @Bean
    public LoginRealm loginRealm(@Qualifier("matcher") HashedCredentialsMatcher matcher){
        LoginRealm loginRealm = new LoginRealm();
        loginRealm.setCredentialsMatcher(matcher);
        return loginRealm;
    }
    @Bean("JwtRealm")
    public JwtRealm jwtRealm(){
        return new JwtRealm();
    }
    @Bean
    public UserModularRealmAuthenticator userModularRealmAuthenticator(){
        UserModularRealmAuthenticator userModularRealmAuthenticator = new UserModularRealmAuthenticator();
        userModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return userModularRealmAuthenticator;
    }
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
}
