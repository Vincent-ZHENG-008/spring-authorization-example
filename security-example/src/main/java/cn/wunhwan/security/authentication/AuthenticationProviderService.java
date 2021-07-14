package cn.wunhwan.security.authentication;

import cn.wunhwan.security.model.AccountModel;
import cn.wunhwan.security.repository.AccountModelRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * 身份认证校验器
 *
 * @author WunHwan
 * @since todo...
 **/
@Component
public class AuthenticationProviderService implements AuthenticationProvider {

  private static final Class<PreAuthenticatedAuthenticationToken> SUPPORTS_CLASS = PreAuthenticatedAuthenticationToken.class;

  private AccountModelRepository repository;

  @Autowired
  public void setRepository(AccountModelRepository repository) {
    this.repository = repository;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final String phone = getPhone(authentication);
    final String password = getPassword(authentication);

    Optional<AccountModel> model = repository.findFirstByPhoneAndPassword(phone, password);
    if (model.isEmpty()) {
      throw new UsernameNotFoundException("账号不存在");
    }

    AccountModel accountModel = model.get();
    String modelPassword = accountModel.getPassword();

    if (Objects.isNull(modelPassword) || modelPassword.equals(password)) {
      throw new BadCredentialsException("密码不正确");
    }

    return new UsernamePasswordAuthenticationToken(
        accountModel.getPhone(), accountModel.getPassword(),
        AuthorityUtils.createAuthorityList(accountModel.getRole())
    );
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return SUPPORTS_CLASS.isAssignableFrom(clazz);
  }

  private static String getPhone(Authentication authentication) {
    Object principal = authentication.getPrincipal();

    if (Objects.isNull(principal)) {
      throw new UsernameNotFoundException("请输入手机号码");
    }
    return (String) principal;
  }

  private static String getPassword(Authentication authentication) {
    Object credentials = authentication.getCredentials();

    if (Objects.isNull(credentials)) {
      throw new BadCredentialsException("请输入密码");
    }
    return (String) credentials;
  }
}
