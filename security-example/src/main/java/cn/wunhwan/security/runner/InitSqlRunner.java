package cn.wunhwan.security.runner;

import cn.wunhwan.security.model.AccountModel;
import cn.wunhwan.security.repository.AccountModelRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * todo...
 *
 * @author WunHwan
 * @since todo...
 */
@Service
public class InitSqlRunner implements ApplicationRunner {

  private static final List<AccountModel> DATA = new ArrayList<>(3);

  static {
    AccountModel model1 = new AccountModel();
    model1.setId(1L);
    model1.setPhone("123455678");
    model1.setPassword("123");
    DATA.add(model1);

    AccountModel model2 = new AccountModel();
    model2.setId(2L);
    model2.setPhone("123455687");
    model2.setPassword("123");
    DATA.add(model2);

    AccountModel model3 = new AccountModel();
    model3.setId(3L);
    model3.setPhone("123455876");
    model3.setPassword("123");
    DATA.add(model3);
  }

  private AccountModelRepository repository;

  @Autowired
  public void setRepository(AccountModelRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void run(ApplicationArguments args) {
    repository.saveAll(DATA);
  }
}
