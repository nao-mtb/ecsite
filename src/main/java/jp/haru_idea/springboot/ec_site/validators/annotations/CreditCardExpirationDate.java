package jp.haru_idea.springboot.ec_site.validators.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import static java.lang.annotation.ElementType.*;

import jp.haru_idea.springboot.ec_site.validators.validators.CreditCardExpirationDateValidator;

@Constraint(validatedBy = CreditCardExpirationDateValidator.class)
@Target({METHOD, FIELD, PARAMETER, TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface CreditCardExpirationDate {
    String message() default "カード情報の有効期限が切れています";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
    // int expYear();
    // int expMonth();
}
