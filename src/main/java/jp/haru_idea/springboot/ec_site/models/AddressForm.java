package jp.haru_idea.springboot.ec_site.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;

public class AddressForm {

    @NotBlank
    private String lastName;

    @NotBlank
    private String firstName;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String prefecture;

    @NotBlank
    private String city;

    @NotBlank
    private String address1;

    private String address2;

    @NotBlank
    private String tel;

    @NotNull
    private int addressType;

    private int shippingDefault;

    private int billingDefault;

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
}
