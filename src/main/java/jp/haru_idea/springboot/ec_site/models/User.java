package jp.haru_idea.springboot.ec_site.models;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.ToString;

@Entity
@Proxy(lazy=false)
@Table(name="users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 64, nullable = false )
    private String lastName;

    @Column(length = 64, nullable = false )
    private String firstName;

    @Column(unique = true, length = 128, nullable = false )
    private String mail;

    // @NotNull
    // @Temporal(TemporalType.DATE)
    // @DateTimeFormat(pattern = "[yyyy/MM/dd][yyyy-MM-dd]")
    // @Column(nullable = false )
    //private Date birthDate;

    @Column(nullable = false )
    private LocalDate birthDate;
    
    @Column(nullable = false )
    private String password;

    @Column(nullable = false , columnDefinition = "int default 0")
    private Integer deleteFlag = 0;

    @Column(nullable = false , columnDefinition = "boolean default false")
    private boolean verified = false;

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

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<CreditCard> creditCards;

    @OneToMany(mappedBy = "user")
    private Collection<Order> orders;
    
    @OneToMany(mappedBy = "user")
    private Collection<RoleUser> roleUsers;

    @OneToOne(mappedBy = "user")
    private Cart cart;

    @OneToOne(mappedBy = "user")
    private Token token;

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
    
    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
    
    @Override
    public String toString() {
        return "User [id=" + id + ", lastName=" + lastName + ", firstName=" + firstName + ", mail=" + mail
                + ", birthDate=" + birthDate + ", password=" + password + ", deleteFlag=" + deleteFlag + ", createdAt="
                + createdAt + ", updatedAt=" + updatedAt + ", version=" + version + ", addresses=" + ", creditCards=" + "]";
    }   
}
