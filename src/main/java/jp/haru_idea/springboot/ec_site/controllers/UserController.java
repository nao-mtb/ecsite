package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Collection;
import java.util.function.BiPredicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import jp.haru_idea.springboot.ec_site.models.UserForm;
import jp.haru_idea.springboot.ec_site.services.UserService;

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

    @GetMapping("/{id}/profile/edit/main")
    public String editMain(Model model, @PathVariable int id){
        User user = userService.getById(id);
        UserForm userForm = convertUserForm(user);
        model.addAttribute("userForm", userForm);
        return "users/edits/main";
    }
    
    @GetMapping("/{id}/profile/edit/password")
    public String editPassword(Model model, @PathVariable int id){
        model.addAttribute("user", userService.getById(id));
        
        return "users/edits/password";
    }



    @PatchMapping("/{id}/profile/update/main")
    public String updateMain(
            @PathVariable int id,
            @Validated
            @ModelAttribute UserForm userForm,
            BindingResult result, Model model,
            RedirectAttributes attrs){
        if(result.hasErrors()){
            return "users/" + id + "/profile/edit/main";
        }    
        User user = formToUser(userForm);
        userService.save(user);
        attrs.addFlashAttribute("success","データの更新に成功しました");    
        return "redirect:/user/" + id + "/profile";
    }


    @GetMapping("/index")
    public String index(Model model){
        Collection<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "users/index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute User user, Model model){
        return "users/create";
    }

    @PostMapping("/save")
    public String save(
        @Validated 
        @ModelAttribute User user,
        BindingResult result, Model model,
        RedirectAttributes attrs){
        if(result.hasErrors()){
            return "users/create";
        }
        userService.save(user);
        attrs.addFlashAttribute("success","データの登録に成功しました");
        return "redirect:/user/index";    
    }
    
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        User user = userService.getById(id);
        //model.addAttribute("user", user);
        UserForm userForm = convertUserForm(user);
        model.addAttribute("userForm", userForm);
        return "users/edit";
    }

    @PatchMapping("/update/{id}")
    public String update(
        @PathVariable int id,
        @Validated
        @ModelAttribute UserForm userForm,
        BindingResult result, Model model,
        RedirectAttributes attrs
    ){
        
        User user = formToUser(userForm);
        userService.save(user);
        attrs.addFlashAttribute("success", "データの更新に成功しました");
        return "redirect:/user/index";

    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable int id, Model model){
        User user = userService.getById(id);
        UserForm userForm = convertUserForm(user);
        model.addAttribute("userForm", userForm);
        return "users/show";    
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model, RedirectAttributes attrs){
        userService.delete(id);
        attrs.addFlashAttribute("success","データの削除に成功しました");
        return "redirect:/user/index";
    }

    private UserForm convertUserForm(User user){
        UserForm userForm = new UserForm();
        userForm.setId(user.getId());
        userForm.setLastName(user.getLastName());
        userForm.setFirstName(user.getFirstName());
        userForm.setMail(user.getMail());
        userForm.setBirthDate(user.getBirthDate());
        return userForm;
    }        

    private User formToUser(UserForm userForm){
        User user = userService.getById(userForm.getId());
        user.setLastName(userForm.getLastName().replace("　","").replace(" ","").trim());
        user.setFirstName(userForm.getFirstName().replace("　","").replace(" ", "").trim());
        user.setMail(userForm.getMail());
        user.setBirthDate(user.getBirthDate());
        return user;
    }
    

}
