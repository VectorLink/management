package com.hair.management.bean.shiro;

import cn.hutool.json.JSONUtil;
import com.hair.management.bean.Constants;
import com.hair.management.bean.response.ApiResult;
import com.hair.management.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Component
@Slf4j
public class JwtFilter extends AuthenticatingFilter {
    @Resource
    private JwtUtils jwtUtils;
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwtToken = request.getHeader(Constants.AUTHORIZATION);
        if (StringUtils.isEmpty(jwtToken)){
            return null;
        }
        return new JwtToken(jwtToken);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = WebUtils.toHttp(servletRequest);
        String jwtToken = request.getHeader(Constants.AUTHORIZATION);
        if (StringUtils.isEmpty(jwtToken)){
            return true ;
        }
        Claims claimByToken = jwtUtils.getClaimByToken(jwtToken);
        if (Objects.isNull(claimByToken)||jwtUtils.isTokenExpired(LocalDateTime.ofInstant(claimByToken.getExpiration().toInstant(), ZoneId.systemDefault()))){
            throw new ExpiredCredentialsException("token 失效，请重新登录");
        }
        return executeLogin(servletRequest,servletResponse);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        ApiResult<Object> error = ApiResult.error(e.getMessage());
        try {
            response.getWriter().print(JSONUtil.toJsonStr(error));
        } catch (IOException ex) {
           log.error("登录异常：",ex);
        }
        return false;

    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-Control-Allow-Origin",httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,OPTIONS");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())){
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.onPreHandle(request, response, mappedValue);
    }

}
