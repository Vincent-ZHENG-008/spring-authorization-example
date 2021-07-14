package cn.wunhwan.security.validation;

import cn.wunhwan.security.model.LoginDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 密码登录校验器
 *
 * @author WunHwan
 */
@SuppressWarnings("ALL")
@Component
public class NormalLoginValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return LoginDTO.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    LoginDTO dto = (LoginDTO) target;

    if (StringUtils.isEmpty(dto.getPhone())) {
      errors.rejectValue("phone", "not-empy", "手机号不能为空");
      return;
    }

    if (StringUtils.isEmpty(dto.getPassword())) {
      errors.rejectValue("password", "not-empy", "密码不能为空");
      return;
    }
  }
}
