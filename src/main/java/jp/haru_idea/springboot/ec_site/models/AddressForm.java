package jp.haru_idea.springboot.ec_site.models;

public class AddressForm {
    private int id;

    private String lastName;

    private String firstName;

    private String zipCode;

    private String prefecture;

    private String city;

    private String address1;

    private String address2;

    private String tel;

    private int addressType;

    private int shippingDefault;

    private int billingDefault;

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
