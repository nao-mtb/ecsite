package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Collection;
import java.util.function.BiPredicate;

import javax.annotation.security.RolesAllowed;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.models.UserAdminForm;
import jp.haru_idea.springboot.ec_site.models.UserCreateForm;
import jp.haru_idea.springboot.ec_site.models.UserCommonForm;
import jp.haru_idea.springboot.ec_site.services.UserService;
import net.bytebuddy.jar.asm.commons.ModuleRemapper;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/{id}/profile")
    public String profile(Model model, @PathVariable int id){
        model.addAttribute("user", userService.getById(id));
        return "users/profile";
    }

    //編集
    @GetMapping("/{id}/profile/main/edit")
    public String editMain(Model model, @PathVariable int id){
        User user = userService.getById(id);
        UserCommonForm userCommonForm = convertUserCommonForm(user);
        model.addAttribute("userCommonForm", userCommonForm);
        return "users/edit";
    }
    
    @PatchMapping("/{id}/profile/main/update")
    public String updateMain(
            @PathVariable int id,
            @Validated
            @ModelAttribute UserCommonForm userCommonForm,
            BindingResult result, Model model,
            RedirectAttributes attrs){
        if(result.hasErrors()){
            return "users/" + id + "/profile/main/edit";
        }
        User user = commonFormToUser(userCommonForm, userService.getById(id));
        userService.save(user);
        attrs.addFlashAttribute("success","データの更新に成功しました");    
        return "redirect:/user/" + id + "/profile";
    }

    //パスワード変更
    @GetMapping("/{id}/profile/password/edit")
    public String editPassword(Model model, @PathVariable int id){
        model.addAttribute("user", userService.getById(id));
        
        return "users/passwords/edit";
    }

    //退会処理
    @GetMapping("/{id}/profile/delete/")
    public String confirmDelete(Model model, @PathVariable int id){
        UserCommonForm userCommonForm = convertUserCommonForm(userService.getById(id));
        model.addAttribute("userCommonForm", userCommonForm);
        return "users/delete";
    }

    //TODO return先をホーム画面に変更
    @PatchMapping("/{id}/profile/user-deleted/")
    public String delete(
            @PathVariable int id, 
            @ModelAttribute UserCommonForm userCommonForm,
            Model model, RedirectAttributes attrs){
        User user = commonFormToUser(userCommonForm, userService.getById(id));
        user.setDeleteFlag(1);
        userService.save(user);
        attrs.addFlashAttribute("success","ご利用ありがとうございました");    
        return "redirect:/user/" + id + "/profile";
    }


    //TODO 一覧表示もformを使用
    // @RolesAllowed("ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    // // @PreAuthorize("hasAnyRole('ADMIN','SECURITY', 'OWNER')")
    @GetMapping("/index")
    public String index(Model model){
        //Collection<User> users = userService.getAll();
        Collection<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "users/admins/index";
    }

    //新規作成
    //TODO ロール紐づけ
    @GetMapping("/create")
    public String create(@ModelAttribute UserCreateForm userCreateForm, Model model){
        return "users/create";
    }

    @PostMapping("/save")
    public String save(
        @Validated 
        @ModelAttribute UserCreateForm userCreateForm,
        BindingResult result, Model model,
        RedirectAttributes attrs){
        if(result.hasErrors()){
            return "users/create";
        }
        User user = createFormToUser(userCreateForm, new User());
        userService.save(user);
        attrs.addFlashAttribute("success","データの登録に成功しました");
        return "redirect:/user/address/create/" + user.getId();
    }

    //管理者用編集
    @PreAuthorize("hasAnyRole('SECURITY', 'OWNER')")
    @GetMapping("/admin/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        User user = userService.getById(id);
        UserAdminForm userAdminForm = convertUserAdminForm(user);
        model.addAttribute("id", id);
        model.addAttribute("userAdminForm", userAdminForm);
        return "users/admins/edit";
    }

    //管理者用更新
    @PreAuthorize("hasAnyRole('SECURITY', 'OWNER')")
    @PatchMapping("/admin/update/{id}")
    public String update(
        @PathVariable int id,
        @Validated
        @ModelAttribute UserAdminForm userAdminForm,
        BindingResult result, Model model,
        RedirectAttributes attrs
    ){
        User user = adminFormToUser(userAdminForm, userService.getById(id));
        userService.save(user);
        attrs.addFlashAttribute("success", "データの更新に成功しました");
        return "redirect:/user/index";
    }

    @GetMapping("/admin/show/{id}")
    public String show(@PathVariable int id, Model model){
        User user = userService.getById(id);
        UserAdminForm userAdminForm = convertUserAdminForm(user);
        model.addAttribute("id", user.getId());
        model.addAttribute("userAdminForm", userAdminForm);
        return "users/admins/show";    
    }

    @DeleteMapping("/admin/delete/{id}")
    public String delete(@PathVariable int id, Model model, RedirectAttributes attrs){
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
        user.setPassword(userCreateForm.getPassword());
        return user;
    }

}
