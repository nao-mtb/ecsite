package jp.haru_idea.springboot.ec_site.validators.validators;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jp.haru_idea.springboot.ec_site.models.UserCommonForm;
import jp.haru_idea.springboot.ec_site.validators.annotations.AgeVerification;

public class AgeVerificationValidator implements ConstraintValidator<AgeVerification, Object> {
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context){
        LocalDate birthDate;
        
        if(object instanceof UserCommonForm){
            UserCommonForm userCommonForm = (UserCommonForm)object;
            birthDate = userCommonForm.getBirthDate();
            if(birthDate == null){
                return true;
            }
        }else{
            return true;
        }

        LocalDate today = LocalDate.now();
        return !birthDate.plusYears(18).isAfter(today);
    }

}
