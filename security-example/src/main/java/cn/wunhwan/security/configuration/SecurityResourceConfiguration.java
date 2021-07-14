package cn.wunhwan.security.configuration;

import cn.wunhwan.security.authentication.AuthenticationProcessingFilter;
import cn.wunhwan.security.authorization.AuthorizationOncePerRequestFilter;
import cn.wunhwan.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity 配置类
 *
 * @author WunHwan
 * @since todo...
 **/
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityResourceConfiguration extends WebSecurityConfigurerAdapter {

  /**
   * 需要在指定对象前执行的类
   */
  private static final Class<UsernamePasswordAuthenticationFilter> AUTHENTICATION_FILTER_CLASS = UsernamePasswordAuthenticationFilter.class;

  @Autowired
  private AuthenticationProvider authenticationProvider;
  @Autowired
  private AuthenticationEntryPoint authenticationEntryPoint;
  @Autowired
  private AccessDeniedHandler accessDeniedHandler;
  @Autowired
  private AuthorizationOncePerRequestFilter authorizationOncePerRequestFilter;
  @Autowired
  private SecurityProperties securityProperties;
  @Autowired
  private AuthenticationProcessingFilter authenticationProcessingFilter;

  @Bean
  @Override
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    /* 关闭 csrf 支持 */
    http.csrf().disable();
    /* 开启 cors 支持 */
    http.cors();

    /* 设置身份鉴权逻辑组件 */
    http.addFilterBefore(authorizationOncePerRequestFilter, AUTHENTICATION_FILTER_CLASS);

    /* 设置身份认证逻辑组件 */
    http.addFilterBefore(authenticationProcessingFilter, AUTHENTICATION_FILTER_CLASS);

    /* 设置认证逻辑组件 */
    http.authenticationProvider(authenticationProvider);

    http.authorizeRequests(authorize -> authorize
        /* 指标不拦截，断点暴露通过控制指标端点 includes 来控制 */
        .mvcMatchers("/actuator/**").anonymous()
        /* 认证白名单 */
        .mvcMatchers(getAuthenticationExcludedUrl(securityProperties)).anonymous()
        /* 其他地址都需要认证 */
        .anyRequest().authenticated()
    );

    /* 关闭 Http-Basic 认证和 form-login 登录 */
    http.httpBasic().disable();
    http.formLogin().disable();
    http.logout().disable();

    /* 设置 Session 策略为无状态 */
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    /* 设置认证异常处理 */
    http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    /* 设置鉴权异常处理 */
    http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
  }

  /**
   * 获取不需要认证白名单
   *
   * @param properties 配置数据源
   * @return 白名单集合
   */
  private static String[] getAuthenticationExcludedUrl(SecurityProperties properties) {
    /* 白名单 */
    return properties.getAuthorizationExcludedUrl().toArray(String[]::new);
  }
}
