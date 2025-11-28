package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.haru_idea.springboot.ec_site.models.EndUserSearchForm;
import jp.haru_idea.springboot.ec_site.models.InternalUserSearchForm;
import jp.haru_idea.springboot.ec_site.models.MemberRank;
import jp.haru_idea.springboot.ec_site.models.Role;
import jp.haru_idea.springboot.ec_site.models.RoleType;
import jp.haru_idea.springboot.ec_site.models.RoleUser;
import jp.haru_idea.springboot.ec_site.models.Token;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.models.UserAdminForm;
import jp.haru_idea.springboot.ec_site.models.UserCreateForm;
import jp.haru_idea.springboot.ec_site.models.UserMailForm;
import jp.haru_idea.springboot.ec_site.models.UserResetPasswordForm;
import jp.haru_idea.springboot.ec_site.models.UserChangePasswordForm;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.models.UserCommonForm;
import jp.haru_idea.springboot.ec_site.services.MailService;
import jp.haru_idea.springboot.ec_site.services.RoleUserService;
import jp.haru_idea.springboot.ec_site.services.TokenService;
import jp.haru_idea.springboot.ec_site.services.UserService;
import jp.haru_idea.springboot.ec_site.services.MemberRankService;
import jp.haru_idea.springboot.ec_site.services.RoleService;

@RequestMapping("/user")
// @SessionAttributes("mail")
@Controller
public class UserController {

    private static final RoleType endUser = RoleType.ROLE_ENDUSER;

    @Autowired
    private UserService userService;

    @Autowired
    private SecuritySession securitySession;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MailService mailService;

    @Autowired
    private MemberRankService memberRankService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/profile/main/info")
    public String profile(Model model){
        int userId = securitySession.getUserId();
        model.addAttribute("user", userService.getById(userId));
        return "users/info";
    }

    //新規作成
    //TODO ロール紐づけ
    //TODO 生年月日制御
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
        Map<String, String> contents = mailService.createTokenMailContent("register", tokenStr);
        mailService.sendMail(user.getMail(), contents.get("subject"), contents.get("message"));
        tokenService.processSaveToken(user, tokenStr);
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
        Map<String, String> contents = mailService.createTokenMailContent("register", tokenStr);
        mailService.sendMail(user.getMail(), contents.get("subject"), contents.get("message"));
        tokenService.processSaveToken(user, tokenStr);
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
        roleUserService.addRoleUser(user, endUser);
        tokenService.deleteById(token.getId());
        attrs.addFlashAttribute("success","認証に成功しました");
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

    //TODO 氏名変更時のヘッダーメニューの氏名反映
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
            Map<String, String> contents = mailService.createTokenMailContent("resetPassword", tokenStr);
            mailService.sendMail(user.getMail(), contents.get("subject"), contents.get("message"));
            tokenService.processSaveToken(user, tokenStr);
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
        return "redirect:/user/login";
    }

    //************ 管理者用 ************
    //TODO 一覧表示もformを使用
    //display all users that have authority except endUser
    @GetMapping("/admin/index/authorized-members")
    public String indexWithRoles(Model model){
        Collection<User> roleUsers = userService.getUsersByNotRoleType(endUser);
        model.addAttribute("roleUsers", roleUsers);
        return "users/admins/index";
    }

    //TODO roleTypeにリンクで飛ばす処理を追加
    //display users that have specific authority except endUser
    @GetMapping("/admin/extract/{roleType}")
    public String extractRoleType(@PathVariable RoleType roleType, Model model){
        if(roleType.equals(endUser)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Collection<User> roleUsers = userService.getUsersByRoleType(roleType);
        model.addAttribute("roleUsers", roleUsers);
        return "users/admins/index";
    }

    //社員テーブルを使用した場合は、社員テーブルのユーザを呼び出すロジックに変更
    //display all users except endUser(both authorised and unauthorised users)
    @GetMapping("/admin/index/internal-members")
    public String index(Model model){
        Collection<User> users = userService.getInternalUsers(endUser);
        model.addAttribute("roleUsers", users);
        return "users/admins/index";
    }

    @GetMapping("/admin/search/customer")
    public String searchEndUser(@ModelAttribute EndUserSearchForm searchForm, Model model){
        Role role = roleService.getByRoleType(endUser);
        // model.addAttribute("role", role);
        searchForm.setRoleId(role.getId());
        return "users/admins/search-customer";
    }

    @GetMapping("/admin/search/member")
    public String searchInternalUser(@ModelAttribute InternalUserSearchForm searchForm, Model model){
        Collection<Role> roles = roleService.getExcludedRoleType(endUser);
        model.addAttribute("roles", roles);
        return "users/admins/search-member";
    }

    @PostMapping("/admin/result/customer/list")
    public String resultEndUsers(
            @Validated
            @ModelAttribute EndUserSearchForm searchForm,
            BindingResult result,
            Model model, RedirectAttributes attrs){
        if(result.hasErrors()){
            Role role = roleService.getById(searchForm.getRoleId());
            model.addAttribute("role", role);
            return "users/admins/search-customer";
        }
        Collection<User> users = userService.searchEndUsers(searchForm.getRoleId(), searchForm.getLastName(), searchForm.getFirstName());
        model.addAttribute("users", users);
        return "users/admins/result-customer";
    }

    @PostMapping("/admin/result/member/list")
    public String resultInternalUsers(
            @Validated
            @ModelAttribute InternalUserSearchForm searchForm,
            BindingResult result,
            Model model){
        if(result.hasErrors()){
            Collection<Role> roles = roleService.getExcludedRoleType(endUser);
            model.addAttribute("roles", roles);
            return "users/admins/search-member";
        }
        Collection<User> users = userService.searchInternalUsers(searchForm.getRoleId(), searchForm.getLastName());
        model.addAttribute("users", users);
        return "users/admins/result-member";
    }

    @GetMapping("/admin/result/detail/{id}")
    public String resultUserDetail(@PathVariable int id, Model model){
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "users/admins/details-customer";
    }

    @GetMapping("/admin/create")
    public String createCorpMember(){
        return "users/admins/create";
    }

    //TODO URL変更(pathvariableでidを直接使用しない)
    //管理者用編集
    @GetMapping("/admin/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        User user = userService.getById(id);
        UserAdminForm userAdminForm = convertUserAdminForm(user);
        model.addAttribute("id", id);
        model.addAttribute("roleTypes", RoleType.values());
        model.addAttribute("userAdminForm", userAdminForm);
        return "users/admins/edit";
    }

    //管理者用更新
    @Transactional
    @PatchMapping("/admin/update/{id}")
    public String update(
            @PathVariable int id,
            @Validated
            @ModelAttribute UserAdminForm userAdminForm,
            @RequestParam(value = "roleTypes", required = false) List<RoleType> roleTypes,
            BindingResult result,
            RedirectAttributes attrs){
        User user = userService.getById(userAdminForm.getId());
        roleUserService.editRoleUser(user, roleTypes);
        attrs.addFlashAttribute("success", "データの更新に成功しました");
        return "redirect:/user/admin/search/member";
    }

    //管理者用無効化
    @GetMapping("/admin/show/{id}")
    public String show(@PathVariable int id, Model model){
        User user = userService.getById(id);
        UserAdminForm userAdminForm = convertUserAdminForm(user);
        model.addAttribute("roleUsers", user.getRoleUsers());
        model.addAttribute("userAdminForm", userAdminForm);
        return "users/admins/show";
    }
    @PatchMapping("/admin/leave/{id}")
    public String leave(@PathVariable int id, RedirectAttributes attrs){
        User user = userService.getById(id);
        roleUserService.deleteRoleUser(user);
        userService.activateAccount(user, false);
        attrs.addFlashAttribute("success","退職処理が完了しました");
        return "redirect:/user/admin/index/internal-members";
    }

    @PatchMapping("/admin/withdraw/{id}")
    public String withdraw(@PathVariable int id, RedirectAttributes attrs){
        User user = userService.getById(id);
        userService.activateAccount(user, false);
        attrs.addFlashAttribute("success","退会処理が完了しました");
        return "redirect:/user/admin/search/customer";
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
        userAdminForm.setRoleUsers(user.getRoleUsers());
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

    // private User adminFormToUser(UserCommonForm userCommonForm, User insertUser){
    //     User user = commonFormToUser(userCommonForm, insertUser);
    //     UserAdminForm userAdminForm = (UserAdminForm) userCommonForm;
    //     user.setId(userAdminForm.getId());
    //     return user;
    // }

    private User createFormToUser(UserCommonForm userCommonForm, User insertUser){
        User user = commonFormToUser(userCommonForm, insertUser);
        UserCreateForm userCreateForm = (UserCreateForm) userCommonForm;
        // user.setPassword(userCreateForm.getPassword());
        user.setPassword(encodePassword(userCreateForm.getPassword()));
        user.setMemberRank(memberRankService.getById(1));
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
