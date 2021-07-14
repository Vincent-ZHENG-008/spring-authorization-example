package cn.wunhwan.security.properties;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * 权限配置源
 *
 * @author WunHwan
 * @since todo...
 **/
@Validated
@ConfigurationProperties(prefix = "mcmc.spring.security")
public class SecurityProperties {

  /**
   * 身份认证地址
   */
  private String authenticationUrl;

  /**
   * 忽略鉴权地址
   */
  private List<String> authorizationExcludedUrl = new ArrayList<>();

  /**
   * Jwt-SignKey
   */
  private String signKey;

  public String getAuthenticationUrl() {
    return authenticationUrl;
  }

  public void setAuthenticationUrl(String authenticationUrl) {
    this.authenticationUrl = authenticationUrl;
  }

  public List<String> getAuthorizationExcludedUrl() {
    return authorizationExcludedUrl;
  }

  public void setAuthorizationExcludedUrl(List<String> authorizationExcludedUrl) {
    this.authorizationExcludedUrl = authorizationExcludedUrl;
  }

  public String getSignKey() {
    return signKey;
  }

  public void setSignKey(String signKey) {
    this.signKey = signKey;
  }
}
