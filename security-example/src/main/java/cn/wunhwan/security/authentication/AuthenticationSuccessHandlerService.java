package cn.wunhwan.security.authentication;

import cn.wunhwan.security.constant.SecurityConstant;
import cn.wunhwan.security.factory.JacksonJsonObjectFactory.Singleton;
import cn.wunhwan.security.properties.SecurityProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 身份认证成功处理
 *
 * @author WunHwan
 * @since todo...
 **/
@Component
public class AuthenticationSuccessHandlerService implements AuthenticationSuccessHandler {

  private byte[] signKey;

  @Autowired
  public void setProperties(SecurityProperties properties) {
    final byte[] bytes = properties.getSignKey().getBytes(SecurityConstant.DEFAULT_CHARSET);

    this.signKey = Objects.requireNonNull(bytes);
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest req, HttpServletResponse resp, Authentication authentication
  ) throws IOException {
    Map<String, Object> body = Map.of(
        "username", authentication.getPrincipal(),
        "roles", authentication.getAuthorities().stream().findFirst().orElseThrow()
    );

    Map<String, Object> content = Map.of(
        "code", HttpStatus.OK.value(),
        "token", generateJwtToken(body, signKey)
    );

    println(resp, content);
  }

  private static String generateJwtToken(Map<String, Object> claims, byte[] signKey) {
    return Jwts.builder()
        .setId(RandomStringUtils.random(32, true, true))
        .addClaims(claims)
        .setExpiration(DateUtils.addDays(new Date(), 1))
        .signWith(Keys.hmacShaKeyFor(signKey))
        .compact();
  }

  /**
   * 响应数据
   *
   * @param resp    HttpServletResponse
   * @param content 内容
   * @throws IOException HttpServletResponse.getWriter().println()
   */
  private void println(HttpServletResponse resp, Object content) throws IOException {
    final String body = Singleton.SINGLETON.getObjectFactory().serializableToJson(content);

    resp.addHeader("Access-Control-Allow-Origin", "*");
    resp.addHeader("Access-Control-Allow-Methods", "*");
    resp.addHeader("Access-Control-Allow-Headers", "*");
    resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
    resp.setCharacterEncoding(SecurityConstant.DEFAULT_CHARSET.displayName());
    resp.getWriter().println(body);
    resp.getWriter().flush();
  }
}
