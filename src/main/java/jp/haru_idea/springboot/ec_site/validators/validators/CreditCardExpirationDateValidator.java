package jp.haru_idea.springboot.ec_site.validators.validators;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jp.haru_idea.springboot.ec_site.models.CreditCard;
import jp.haru_idea.springboot.ec_site.models.CreditCardForm;
import jp.haru_idea.springboot.ec_site.validators.annotations.CreditCardExpirationDate;

public class CreditCardExpirationDateValidator implements ConstraintValidator<CreditCardExpirationDate, Object>{
    //TODO interfaceを使用した書き方に変更
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        int expYear;
        int expMonth;

        if(obj instanceof CreditCard){
            CreditCard creditCard = (CreditCard)obj;
            expYear = creditCard.getExpYear();
            expMonth = creditCard.getExpMonth();
        }else if (obj instanceof CreditCardForm){
            CreditCardForm creditCardForm = (CreditCardForm)obj;
            expYear = creditCardForm.getExpYear();
            expMonth = creditCardForm.getExpMonth();
        }else{
            return true;
        }

        LocalDate today = LocalDate.now();
        LocalDate creditCardDate = LocalDate.of(expYear, expMonth, 1).plusMonths(1);
        return creditCardDate.isAfter(today);
    }

}
