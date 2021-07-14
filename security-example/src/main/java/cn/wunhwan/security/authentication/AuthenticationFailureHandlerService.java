package cn.wunhwan.security.authentication;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * 身份验证失败处理
 *
 * @author WunHwan
 * @since todo...
 **/
@Component
public class AuthenticationFailureHandlerService implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest req, HttpServletResponse resp, AuthenticationException exception
  ) throws IOException {
    if (exception instanceof AuthenticationServiceException) {
      resp.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
      return;
    }

    resp.sendError(HttpStatus.UNAUTHORIZED.value());
  }
}
