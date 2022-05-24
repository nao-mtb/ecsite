package jp.haru_idea.springboot.ec_site.models;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Pattern(regexp = "[^!\"#$%&'()*+,-./:;<=>?@[]^_`{]+$]{0,64}", message = "64文字以内の文字（記号を除く）を入力してください")
    @Column(length = 64, nullable = false )
    private String lastName;

    @NotBlank
    @Pattern(regexp = "[^!\"#$%&'()*+,-./:;<=>?@[]^_`{]+$]{0,64}", message = "64文字以内の文字（記号を除く）を入力してください")
    @Column(length = 64, nullable = false )
    private String firstName;

    //TODO ユニークキー登録時のエラーメッセージを作成
    //TODO メール正規表現整理
    @NotBlank
    @Size(max=128)
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^\\/_`{|}~-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$", message = "メール形式で入力してください")
    @Column(unique = true, length = 128, nullable = false )
    private String mail;

    // @NotNull
    // @Temporal(TemporalType.DATE)
    // @DateTimeFormat(pattern = "[yyyy/MM/dd][yyyy-MM-dd]")
    // @Column(nullable = false )
    //private Date birthDate;

    //TODO バリデーションエラー時のリセット対処
    //"[yyyy-MM-dd][yyyy/MM/dd]"から[yyyy/MM/dd]を削除
    @NotNull
    @DateTimeFormat(pattern = "[yyyy-MM-dd]")
    @Column(nullable = false )
    private LocalDate birthDate;
    

    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z]{6,20}$", message="6文字以上20文字以下の英数字を入力してください")
    @Column(nullable = false )
    private String password;

    @Column(nullable = false , columnDefinition = "int default 0")
    private Integer deleteFlag = 0;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(updatable = false , nullable = false )
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(nullable = false )
    private Date updatedAt;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int version;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<CreditCard> creditCards;

    @OneToMany(mappedBy = "user")
    private Collection<Order> orders;
    
    @OneToMany(mappedBy = "user")
    private Collection<RoleUser> roleUsers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Collection<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Collection<Address> addresses) {
        this.addresses = addresses;
    }

    public Collection<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(Collection<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

    public void setOrders(Collection<Order> orders) {
        this.orders = orders;
    }

    public Collection<RoleUser> getRoleUsers() {
        return roleUsers;
    }

    public void setRoleUsers(Collection<RoleUser> roleUsers) {
        this.roleUsers = roleUsers;
    }

    
}
