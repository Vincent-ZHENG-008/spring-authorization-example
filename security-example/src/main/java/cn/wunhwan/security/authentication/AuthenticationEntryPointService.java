package cn.wunhwan.security.authentication;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * HttpStatus：401 无权访问处理组件
 *
 * @author WunHwan
 * @since todo...
 **/
@Component
public class AuthenticationEntryPointService implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest req, HttpServletResponse resp, AuthenticationException exception
  ) throws IOException {
    resp.addHeader("Access-Control-Allow-Origin", "*");
    resp.addHeader("Access-Control-Allow-Methods", "*");
    resp.addHeader("Access-Control-Allow-Headers", "*");
    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
