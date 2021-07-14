package cn.wunhwan.security.authorization;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 身份鉴权失败处理
 *
 * @author WunHwan
 * @since todo...
 **/
@Component
public class AccessDeniedHandlerService implements AccessDeniedHandler {

  @Override
  public void handle(
      HttpServletRequest req, HttpServletResponse resp, AccessDeniedException exception
  ) throws IOException {
    resp.addHeader("Access-Control-Allow-Origin", "*");
    resp.addHeader("Access-Control-Allow-Methods", "*");
    resp.addHeader("Access-Control-Allow-Headers", "*");
    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
  }
}
