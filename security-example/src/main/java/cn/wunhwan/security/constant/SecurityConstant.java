package cn.wunhwan.security.constant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Spring Security 全局静态属性
 *
 * @author WunHwan
 * @since todo...
 **/
public final class SecurityConstant {

  /**
   * 公共数据编码
   */
  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  /**
   * 认证请求头关键字
   */
  public static final String HEADER_AUTHORIZATION = "authorization";

}
