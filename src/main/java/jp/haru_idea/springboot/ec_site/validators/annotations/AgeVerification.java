package jp.haru_idea.springboot.ec_site.validators.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import jp.haru_idea.springboot.ec_site.validators.validators.AgeVerificationValidator;

@Constraint(validatedBy = AgeVerificationValidator.class)
@Target({METHOD, FIELD, PARAMETER, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeVerification {
    String message() default "会員登録は18歳以上からです";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
}
