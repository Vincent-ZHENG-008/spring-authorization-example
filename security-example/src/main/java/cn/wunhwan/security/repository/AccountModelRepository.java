package cn.wunhwan.security.repository;

import cn.wunhwan.security.model.AccountModel;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * todo...
 *
 * @author WunHwan
 * @since todo...
 */
public interface AccountModelRepository extends CrudRepository<AccountModel, Long> {

  Optional<AccountModel> findFirstByPhoneAndPassword(String phone, String password);

}
