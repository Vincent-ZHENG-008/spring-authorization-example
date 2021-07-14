package cn.wunhwan.security.authentication;

import cn.wunhwan.security.constant.SecurityConstant;
import cn.wunhwan.security.factory.JacksonJsonObjectFactory.Singleton;
import cn.wunhwan.security.model.LoginDTO;
import cn.wunhwan.security.properties.SecurityProperties;
import cn.wunhwan.security.validation.NormalLoginValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

/**
 * 身份认证前置处理器
 *
 * @author WunHwan
 * @since todo...
 **/
@Component
public class AuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

  /**
   * 请求方式
   */
  private static final String HTTP_METHOD = HttpMethod.POST.name();

  /**
   * 登录对象类型
   */
  private static final Class<LoginDTO> LOGIN_CLASS = LoginDTO.class;

  /**
   * 登录参数校验器
   */
  private NormalLoginValidator validator;

  public AuthenticationProcessingFilter() {
    // tip: 先设置默认值，后覆盖原匹配机制
    super("/");
  }

  @Autowired
  public void setManager(AuthenticationManager authenticationManager) {
    setAuthenticationManager(authenticationManager);
  }

  @Autowired
  public void setValidator(NormalLoginValidator validator) {
    this.validator = validator;
  }

  @Autowired
  public void setSecurityProperties(SecurityProperties securityProperties) {
    String authenticationUrl = securityProperties.getAuthenticationUrl();
    RequestMatcher requestMatcher = new AntPathRequestMatcher(authenticationUrl, HTTP_METHOD);

    setRequiresAuthenticationRequestMatcher(requestMatcher);
  }

  @Override
  @Autowired
  public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
    super.setAuthenticationSuccessHandler(successHandler);
  }

  @Override
  @Autowired
  public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
    super.setAuthenticationFailureHandler(failureHandler);
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest req, HttpServletResponse resp
  ) throws AuthenticationException, IOException {
    try {
      /* 校验内容格式 */
      if (!MediaType.APPLICATION_JSON_VALUE.equals(req.getContentType())) {
        resp.sendError(HttpStatus.BAD_REQUEST.value());
        throw new AuthenticationServiceException("Content-Type格式不正确");
      }

      /* 读取请求流 */
      final ServletInputStream inputStream = req.getInputStream();
      final String body = new String(inputStream.readAllBytes(), SecurityConstant.DEFAULT_CHARSET);

      /* 解析请求参数 */
      final LoginDTO login = Singleton.SINGLETON.getObjectFactory().parseToBean(body, LOGIN_CLASS);

      /* 参数校验 */
      Optional<String> errors = validate(login);
      if (errors.isPresent()) {
        throw new AuthenticationServiceException("参数异常");
      }

      final Authentication authResult = new PreAuthenticatedAuthenticationToken(
          login.getPhone(), login.getPassword()
      );
      return this.getAuthenticationManager().authenticate(authResult);
    } catch (RuntimeException exception) {
      Throwable cause = exception.getCause();

      /* 校验是否 JSON 解析有误 */
      if (cause instanceof JsonProcessingException) {
        throw new AuthenticationServiceException("请求参数无法解析");
      }
      throw exception;
    }
  }

  /**
   * 参数校验
   *
   * @param login 登录参数
   * @return 校验结果
   */
  private Optional<String> validate(LoginDTO login) {
    Errors errors = new BeanPropertyBindingResult(login, login.getClass().getName());
    validator.validate(login, errors);

    List<FieldError> fieldErrors = errors.getFieldErrors();
    if (fieldErrors.size() > 0) {
      return Optional.ofNullable(fieldErrors.get(0).getDefaultMessage());
    }
    return Optional.empty();
  }
}
