package cn.wunhwan.security.authorization;

import cn.wunhwan.security.constant.SecurityConstant;
import cn.wunhwan.security.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 用户鉴权前置处理器
 *
 * @author WunHwan
 * @since todo...
 **/
@Component
public class AuthorizationOncePerRequestFilter extends OncePerRequestFilter {

  private SecurityProperties properties;

  @Autowired
  public void setProperties(SecurityProperties properties) {
    this.properties = properties;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain
  ) throws ServletException, IOException {
    final String authorization = req.getHeader(SecurityConstant.HEADER_AUTHORIZATION);

    /* 缺省身份凭证 */
    if (StringUtils.isNotBlank(authorization)) {
      try {
        /* 校验Token有效性 */
        Authentication authentication = verifyToken(authorization);

        /* 存储安全上下文对象 */
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
      } catch (Exception exception) {
        exceptionHandler(resp, exception);
        return;
      }
    }
    filterChain.doFilter(req, resp);
  }

  /**
   * 校验 Token 有效性
   *
   * @param token 身份凭证
   * @return 服务请求结果
   */
  private Authentication verifyToken(String token) {
    JwtParser parser = Jwts.parserBuilder()
        .setSigningKey(properties.getSignKey().getBytes(SecurityConstant.DEFAULT_CHARSET))
        .build();

    Claims body = parser.parseClaimsJws(token).getBody();

    return new UsernamePasswordAuthenticationToken(
        body.get("username", String.class),
        AuthorityUtils.createAuthorityList(body.get("role", String.class))
    );
  }

  private static void exceptionHandler(
      HttpServletResponse response, Exception exception
  ) throws IOException {
    response.addHeader("Access-Control-Allow-Origin", "*");
    response.addHeader("Access-Control-Allow-Methods", "*");
    response.addHeader("Access-Control-Allow-Headers", "*");

    if (exception instanceof AuthenticationCredentialsNotFoundException) {
      response.sendError(HttpStatus.UNAUTHORIZED.value());
      return;
    }

    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }
}
