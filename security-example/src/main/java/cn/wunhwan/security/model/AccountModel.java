package cn.wunhwan.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * todo...
 *
 * @author WunHwan
 * @since todo...
 */
@Entity
@Table("account_model")
public class AccountModel {

  @Id
  @Column
  private Long id;

  @Column
  private String phone;

  @Column
  private String password;

  @Column
  private String role;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
