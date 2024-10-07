package jp.haru_idea.springboot.ec_site.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import jp.haru_idea.springboot.ec_site.validators.annotations.CreditCardExpirationDate;

@CreditCardExpirationDate
public class CreditCardForm {
    private User user;

    @NotBlank
    private String company;

    @NotBlank
    private String cardNumber;

    @NotBlank
    private String name;

    @NotNull
    private int expMonth;

    @NotNull
    private int expYear;

    private int cardDefault;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public int getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(int expMonth) {
        this.expMonth = expMonth;
    }

    public int getExpYear() {
        return expYear;
    }

    public void setExpYear(int expYear) {
        this.expYear = expYear;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardDefault() {
        return cardDefault;
    }

    public void setCardDefault(int cardDefault) {
        this.cardDefault = cardDefault;
    }
    
}
