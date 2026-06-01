package jp.haru_idea.springboot.ec_site.advices;

import jp.haru_idea.springboot.ec_site.services.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jp.haru_idea.springboot.ec_site.models.RoleType;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final RoleUserService roleUserService;

    @Autowired
    private SecuritySession securitySession;

    private static final RoleType endUser = RoleType.ROLE_ENDUSER;

    GlobalExceptionHandler(RoleUserService roleUserService) {
        this.roleUserService = roleUserService;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleError(Exception e, Model model){
        model.addAttribute("errMsg", e.getMessage());
        int userId = securitySession.getUserId();
        if(userId == 0){
            return "errors/info";
        }
        if(roleUserService.hasSpecificRole(userId, endUser)){
            return "errors/info";
        }else{
            return "errors/backoffice-info";
        }
    }
}
