package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiPredicate;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.haru_idea.springboot.ec_site.models.Token;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.models.UserAdminForm;
import jp.haru_idea.springboot.ec_site.models.UserCreateForm;
import jp.haru_idea.springboot.ec_site.models.UserMailForm;
import jp.haru_idea.springboot.ec_site.models.UserResetPasswordForm;
import jp.haru_idea.springboot.ec_site.models.UserChangePasswordForm;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.models.UserCommonForm;
import jp.haru_idea.springboot.ec_site.services.TokenService;
import jp.haru_idea.springboot.ec_site.services.UserService;

@RequestMapping("/user")
// @SessionAttributes("mail")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecuritySession securitySession;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/profile/main/info")
    public String profile(Model model){
        int userId = securitySession.getUserId();
        model.addAttribute("user", userService.getById(userId));
        return "users/info";
    }

    //新規作成
    //TODO ロール紐づけ
    @GetMapping("/create")
    public String createUser(@ModelAttribute UserCreateForm userCreateForm){
        return "users/create";
    }

    @PostMapping("/save")
    public String saveUser(
            @Validated 
            @ModelAttribute UserCreateForm userCreateForm,
            BindingResult result, Model model,
            RedirectAttributes attrs){
        if(result.hasErrors()){
            return "users/create";
        }
        if(userService.isUserExists(userCreateForm.getMail())){
            attrs.addFlashAttribute("error","メールアドレスが既に登録されています");
            model.addAttribute("mail", userCreateForm.getMail());
            return "users/verify-regenerate";
        }
        User user = createFormToUser(userCreateForm, new User());
        userService.save(user);

        // send mail with token
        String tokenStr = UUID.randomUUID().toString();
        userService.sendTokenMail("register",user.getMail(), tokenStr);
        Token token = tokenService.findOrCreateUserToken(user);
        token.setToken(tokenStr);
        tokenService.save(token);
        attrs.addFlashAttribute("success","メールを送信しました。メールのURLからアクセスしなおしてください");
        return "redirect:/user/create/";
    }

    @GetMapping("/create/auth/resend-request")
    public String resendTokenToExistsUser(@RequestParam("mail") String mail, Model model){
        model.addAttribute("mail", mail);
        return "users/verify-regenerate";
    }

    @PatchMapping("/create/auth/resend-completed")
    public String regenerateToken(@RequestParam("mail") String mail, RedirectAttributes attrs){
        User user = userService.getByMail(mail);
        // send mail with token
        String tokenStr = UUID.randomUUID().toString();
        userService.sendTokenMail("register",mail, tokenStr);
        Token token = tokenService.findOrCreateUserToken(user);
        token.setToken(tokenStr);
        tokenService.save(token);
        attrs.addFlashAttribute("success","メールを送信しました。メールのURLからアクセスしなおしてください");
        return "redirect:/user/create/";
    }

    @Transactional
    @GetMapping("/create/auth/verify")
    public String verifyUser(
            @RequestParam("token") String tokenStr,
            Model model, RedirectAttributes attrs){
        Token token = tokenService.getByToken(tokenStr);
        //TODO URL無効時の遷移先変更
        if (token == null){
            attrs.addFlashAttribute("error", "URLが無効です");
            return "redirect:/user/create";
            // return "redirect:/user/create/auth/resend-request";
        }
        String mail = token.getUser().getMail();
        if (!tokenService.isExpirationDate(token.getUpdatedAt())){
            model.addAttribute("mail", mail);
            attrs.addFlashAttribute("error", "URLの有効期限が切れています");
            return "users/verify-regenerate";
        }
        User user = userService.getByMail(mail);
        user.setVerified(true);
        userService.save(user);
        tokenService.deleteById(token.getId());
        attrs.addFlashAttribute("success","認証に成功しました");
        // return "redirect:/user/credit-card/create";
        return "redirect:/user/address/create";
    }

    //編集
    @GetMapping("/profile/main/edit")
    public String editMain(Model model){
        int userId = securitySession.getUserId();
        User user = userService.getById(userId);
        UserCommonForm userCommonForm = convertUserCommonForm(user);
        model.addAttribute("userCommonForm", userCommonForm);
        return "users/edit";
    }

    @PatchMapping("/profile/main/update")
    public String updateMain(
            @Validated
            @ModelAttribute UserCommonForm userCommonForm,
            BindingResult result,
            RedirectAttributes attrs){
        int userId = securitySession.getUserId();
        if(result.hasErrors()){
            return "users/profile/main/edit";
        }
        User user = commonFormToUser(userCommonForm, userService.getById(userId));
        userService.save(user);
        attrs.addFlashAttribute("success","データの更新に成功しました");    
        return "redirect:/user/profile/main/info";
    }

    //パスワード変更
    @GetMapping("/profile/password/change")
    public String changePassword(@ModelAttribute UserChangePasswordForm userChangePasswordForm, Model model){
        int userId = securitySession.getUserId();
        model.addAttribute("user", userService.getById(userId));
        return "users/passwords/change";
    }
    @PatchMapping("/profile/password/update")
    public String updatePassword(
            @Validated
            @ModelAttribute UserChangePasswordForm userChangePasswordForm,
            BindingResult result,
            RedirectAttributes attrs){
        int userId = securitySession.getUserId();
        if(result.hasErrors()){
            return "users/passwords/change";
        }
        User user = userService.getById(userId);

        if(!(passwordEncoder().matches(userChangePasswordForm.getOldPassword(), user.getPassword()))){
            attrs.addFlashAttribute("error", "現在のパスワードが一致しません");
            return "redirect:/user/profile/password/change";
        }
        if(!userChangePasswordForm.isNewPassword()){
            attrs.addFlashAttribute("error", "新しいパスワードと確認用の入力が一致しません");
            return "redirect:/user/profile/password/change";
        }
        user.setPassword(encodePassword(userChangePasswordForm.getPassword()));
        userService.save(user);
        attrs.addFlashAttribute("success","データの更新に成功しました");    
        return "redirect:/user/profile/main/info";
    }

    //パスワード再発行
    @GetMapping("/profile/password/reset/request")
    public String requestResetPassword(@ModelAttribute UserMailForm userMailForm){
        return "users/passwords/reset-request";
    }
    @PatchMapping("/profile/password/reset/accept")
    public String generateTokenForResetPassword(
            @Validated
            @ModelAttribute UserMailForm userMailForm,
            BindingResult result,
            RedirectAttributes attrs){
        User user = userService.getByMail(userMailForm.getMail());
        if (user != null){
            String tokenStr = UUID.randomUUID().toString();
            userService.sendTokenMail("resetPassword",user.getMail(), tokenStr);
            Token token = tokenService.findOrCreateUserToken(user);
            // Token token = tokenService.getByUserId(user.getId());
            // if (token == null){
            //     token = new Token();
            //     token.setUser(user);
            // }
            token.setToken(tokenStr);
            tokenService.save(token);
        }
        attrs.addFlashAttribute("success", "メールを送信しました");
        return "redirect:/user/profile/password/reset/request";
    }

    @GetMapping("/profile/password/reset")
    public String inputResetPassword(
            @RequestParam("token") String tokenStr,
            RedirectAttributes attrs,
            @ModelAttribute UserResetPasswordForm userResetPasswordForm,
            Model model){
        Token token = tokenService.getByToken(tokenStr);
        if (token == null){
            attrs.addFlashAttribute("error", "URLが無効です。再度パスワードリセット登録をしてください。");
            return "redirect:/user/profile/password/reset/request";
        }
        if (!tokenService.isExpirationDate(token.getUpdatedAt())){
            attrs.addFlashAttribute("error", "URLの有効期限が切れています。再度パスワードリセット登録をしてください。");
            return "redirect:/user/profile/password/reset/request";
        }
        model.addAttribute("mail", token.getUser().getMail());
        model.addAttribute("tokenId", token.getId());
        return "users/passwords/reset-token";
    }
    
    @PatchMapping("/profile/password/reset/update")
    public String executeResetPassword(
            @Validated
            @RequestParam("mail") String mail,
            @RequestParam("tokenId") int tokenId,
            @ModelAttribute("userResetPasswordForm") UserResetPasswordForm userResetPasswordForm,
            BindingResult result, Model model,
            RedirectAttributes attrs){
        if(result.hasErrors()){
            return "users/passwords/change";
        }

        //セッションに残した場合
        // String mail = (String)model.getAttribute("mail");

        User user = userService.getByMail(mail);
        if(!userResetPasswordForm.isNewPassword()){
            attrs.addFlashAttribute("error", "新しパスワードと確認用の入力が一致しません");
            return "redirect:/user/profile/password/reset/{}";
        }
        user.setPassword(encodePassword(userResetPasswordForm.getPassword()));
        userService.save(user);
        tokenService.deleteById(tokenId);
        attrs.addFlashAttribute("success","データの更新に成功しました");    
        return "redirect:/user/profile/main/info";
    }

    //退会処理
    @GetMapping("/delete")
    public String confirmDelete(Model model){
        int userId = securitySession.getUserId();
        UserCommonForm userCommonForm = convertUserCommonForm(userService.getById(userId));
        model.addAttribute("userCommonForm", userCommonForm);
        return "users/delete";
    }

    //TODO return先をホーム画面に変更
    @PatchMapping("/user-deleted")
    public String delete(
            @ModelAttribute UserCommonForm userCommonForm,
            RedirectAttributes attrs){
        int userId = securitySession.getUserId();
        User user = commonFormToUser(userCommonForm, userService.getById(userId));
        user.setDeleteFlag(1);
        userService.save(user);
        attrs.addFlashAttribute("success","ご利用ありがとうございました");    
        return "redirect:/user/profile";
    }

    //TODO 一覧表示もformを使用
    //************ 管理者用 ************
    @GetMapping("/admin/index")
    public String index(Model model){
        //Collection<User> users = userService.getAll();
        Collection<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "users/admins/index";
    }

    //管理者用編集
    @GetMapping("/admin/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        User user = userService.getById(id);
        UserAdminForm userAdminForm = convertUserAdminForm(user);
        model.addAttribute("id", id);
        model.addAttribute("userAdminForm", userAdminForm);
        return "users/admins/edit";
    }
    //管理者用更新
    @PatchMapping("/admin/update/{id}")
    public String update(
            @PathVariable int id,
            @Validated
            @ModelAttribute UserAdminForm userAdminForm,
            BindingResult result,
            RedirectAttributes attrs){
        User user = adminFormToUser(userAdminForm, userService.getById(id));
        userService.save(user);
        attrs.addFlashAttribute("success", "データの更新に成功しました");
        return "redirect:/user/index";
    }

    //管理者用削除
    @GetMapping("/admin/show/{id}")
    public String show(@PathVariable int id, Model model){
        User user = userService.getById(id);
        UserAdminForm userAdminForm = convertUserAdminForm(user);
        model.addAttribute("id", user.getId());
        model.addAttribute("userAdminForm", userAdminForm);
        return "users/admins/show";    
    }
    @DeleteMapping("/admin/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes attrs){
        userService.delete(id);
        attrs.addFlashAttribute("success","データの削除に成功しました");
        return "redirect:/user/index";
    }

    private UserCommonForm convertUserCommonForm(User user){
        UserCommonForm userCommonForm = new UserCommonForm();
        userCommonForm.setLastName(user.getLastName());
        userCommonForm.setFirstName(user.getFirstName());
        userCommonForm.setMail(user.getMail());
        userCommonForm.setBirthDate(user.getBirthDate());
        userCommonForm.setDeleteFlag(user.getDeleteFlag());
        return userCommonForm;
    }        

    private UserAdminForm convertUserAdminForm(User user){
        UserAdminForm userAdminForm = new UserAdminForm();
        userAdminForm.setId(user.getId());
        userAdminForm.setLastName(user.getLastName());
        userAdminForm.setFirstName(user.getFirstName());
        userAdminForm.setMail(user.getMail());
        userAdminForm.setBirthDate(user.getBirthDate());
        userAdminForm.setDeleteFlag(user.getDeleteFlag());
        return userAdminForm;
    }
    
    private User commonFormToUser(UserCommonForm userCommonForm, User user){    
        user.setLastName(userCommonForm.getLastName().replace("　","").replace(" ","").trim());
        user.setFirstName(userCommonForm.getFirstName().replace("　","").replace(" ", "").trim());
        user.setMail(userCommonForm.getMail());
        user.setBirthDate(userCommonForm.getBirthDate());
        user.setDeleteFlag(userCommonForm.getDeleteFlag());
        return user;
    }

    private User adminFormToUser(UserCommonForm userCommonForm, User insertUser){
        User user = commonFormToUser(userCommonForm, insertUser);
        UserAdminForm userAdminForm = (UserAdminForm) userCommonForm;
        user.setId(userAdminForm.getId()); 
        return user;
    }

    private User createFormToUser(UserCommonForm userCommonForm, User insertUser){
        User user = commonFormToUser(userCommonForm, insertUser);
        UserCreateForm userCreateForm = (UserCreateForm) userCommonForm;
        // user.setPassword(userCreateForm.getPassword());
        user.setPassword(encodePassword(userCreateForm.getPassword()));
        return user;
    }

    // private User changePasswordFormToUser(UserCommonForm userCommonForm, User insertUser){
    //     User user = commonFormToUser(userCommonForm, insertUser);
    //     UserChangePasswordForm userChangePasswordForm = (UserChangePasswordForm) userCommonForm;
    //     user.setPassword(encodePassword(userChangePasswordForm.getPassword()));
    //     return user;
    // }
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    public String encodePassword(String password){
        return passwordEncoder().encode(password);
    }
}
