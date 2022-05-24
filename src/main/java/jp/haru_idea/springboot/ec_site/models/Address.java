package jp.haru_idea.springboot.ec_site.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name="addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @NotBlank
    @Column(length = 64, nullable = false)
    private String lastName;

    @NotBlank
    @Column(length = 64, nullable = false)
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[0-9]{3}-[0-9]{4}$", message="3桁の数字、ハイフン、4桁の数字で入力してください")
    @Column(length = 8, nullable = false)
    private String zipCode;

    @NotBlank
    @Column(length = 64, nullable = false)
    private String prefecture;

    @NotBlank
    @Column(length = 64, nullable = false)
    private String city;

    @NotBlank
    @Column(length = 128, nullable = false)
    private String address1;

    @Column(length = 128)
    private String address2;

    @NotBlank
    @Column(length = 64, nullable = false)
    private String tel;

    @NotNull
    private int addressType;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int shippingDefault = 0;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int billingDefault = 0;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPrefecture() {
        return prefecture;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public int getShippingDefault() {
        return shippingDefault;
    }

    public void setShippingDefault(int shippingDefault) {
        this.shippingDefault = shippingDefault;
    }

    public int getBillingDefault() {
        return billingDefault;
    }

    public void setBillingDefault(int billingDefault) {
        this.billingDefault = billingDefault;
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

    
}
