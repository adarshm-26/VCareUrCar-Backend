package com.car.auth.components;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

public class HeaderFilter extends ZuulFilter {

  @Autowired
  JwtTokenUtil tokenUtil;
  @Autowired
  JwtUserDetailsService userDetailsService;

  @Override
  public boolean shouldFilter() {
    String uri = RequestContext.getCurrentContext().getRequest().getRequestURI();
    if (uri.equalsIgnoreCase("/authenticate")) return false;
    else if (uri.equalsIgnoreCase("/register")) return false;
    else return true;
  }

  @Override
  public String filterType() {
    return PRE_TYPE;
  }

  @Override
  public int filterOrder() {
    return PRE_DECORATION_FILTER_ORDER - 1;
  }

  @Override
  public Object run() throws ZuulException {
    HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
    String token = request.getHeader("Authorization");
    if (token == null || !token.startsWith("Bearer ")) {
      RequestContext.getCurrentContext().setSendZuulResponse(false);
    }
    else {
      token = token.substring(7);
      if (tokenUtil.validateToken(token, userDetailsService
          .loadUserByUsername(tokenUtil.getUsernameFromToken(token))) &&
          !tokenUtil.isTokenExpired(token)) {
        String role = (String) tokenUtil.getAllClaimsFromToken(token).get("role");
        request.setAttribute("role", role);
        RequestContext.getCurrentContext().addZuulRequestHeader("role", role);
      }
      else {
        RequestContext.getCurrentContext().setSendZuulResponse(false);
      }
    }
    return null;
  }

}
