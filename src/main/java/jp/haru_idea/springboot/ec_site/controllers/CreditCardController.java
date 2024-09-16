package jp.haru_idea.springboot.ec_site.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.haru_idea.springboot.ec_site.models.CreditCard;
import jp.haru_idea.springboot.ec_site.models.CreditCardForm;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.models.UserAdminForm;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.services.CreditCardService;
import jp.haru_idea.springboot.ec_site.services.UserService;

@RequestMapping("/user")
@Controller
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private SecuritySession securitySession;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @GetMapping("/profile/credit-card/info")
    public String profile(Model model){
        int userId = securitySession.getUserId();
        if (userId == 0){
            return "users/login";
        }
        model.addAttribute("user", userService.getById(userId));
        return "creditCards/info";
    }

    @GetMapping("/credit-card/create")
    public String create(
            @ModelAttribute CreditCard creditCard,
            @SessionAttribute("userId") int userId, Model model){
        User user = userService.getById(userId);
        creditCard.setUser(user);
        int currentYear = currentYear();
        model.addAttribute("year", currentYear);
        return "creditCards/create";
    }
    @PostMapping("/credit-card/save")
    public String save(
            @Validated
            @ModelAttribute @RequestBody CreditCard creditCard,
            BindingResult result,
            RedirectAttributes attrs){
        // int userId = securitySession.getUserId();
        // if (userId == 0){
        //     return "users/login";
        // }
        if(result.hasErrors()){
            return "creditCards/create";
        }
        creditCardService.save(creditCard);
        attrs.addFlashAttribute("success","カード情報の登録に成功しました");
        return "redirect:/user/credit-card/create";
    }
    
    // @GetMapping({"/profile/credit-card/edit1/{creditCardId}","/profile/credit-card/edit2/{creditCardId}"})
    @GetMapping("/profile/credit-card/edit/{creditCardId}")
    public String editCreditCard(
            @PathVariable int creditCardId, 
            @RequestParam("source") String source,
            Model model){
        CreditCard creditCard = creditCardService.getById(creditCardId);
        CreditCardForm creditCardForm = convertCreditCardForm(creditCard);
        int userId = securitySession.getUserId();
        if (userId == 0){
            return "users/login";
        }
        int currentYear = currentYear();
        model.addAttribute("userId", userId);
        model.addAttribute("creditCardForm", creditCardForm);
        model.addAttribute("year", currentYear);
        model.addAttribute("source", source);
        return "creditCards/edit";
    }
    
    @PatchMapping("/profile/credit-card/update/{creditCardId}")
    public String updateCreditCard(
            @PathVariable int creditCardId,
            @RequestParam("source") String source,
            @Validated
            @ModelAttribute @RequestBody CreditCardForm creditCardForm,
            BindingResult result,
            RedirectAttributes attrs){
        int userId = securitySession.getUserId();
        if (userId == 0){
            return "users/login";
        }
        if(result.hasErrors()){
            return "creditCards/edit";
        }
        CreditCard creditCard = formToCreditCard(creditCardForm, creditCardId);
        creditCardService.save(creditCard);
        attrs.addFlashAttribute("success","データの更新に成功しました");
        if(source.equals("info")){
            return "redirect:/user/profile/credit-card/info";
        }
        return "redirect:/cart/view";
    }

    //TODO Javascriptを用いてポップアップ画面からの削除
    @GetMapping("/profile/credit-card/delete/{creditCardId}")
    public String confirmDelete(
            @PathVariable int creditCardId,
            @RequestParam("source") String source,
            Model model){
        int userId = securitySession.getUserId();
        if (userId == 0){
            return "users/login";
        }
        CreditCard creditCard = creditCardService.getById(creditCardId);
        int cardUserId = creditCard.getUser().getId();
        if (userId != cardUserId){
            return "users/login";
        }
        model.addAttribute("creditCard", creditCard);
        model.addAttribute("source", source);
        return "creditCards/delete";
    }

    @DeleteMapping("/profile/credit-card/delete2/{creditCardId}")
    public String delete(
            @PathVariable int creditCardId,
            @RequestParam("source") String source,
            @RequestParam("select") String select,
            RedirectAttributes attrs){
        int userId = securitySession.getUserId();
        if (userId == 0){
            return "users/login";
        }
        if(select.equals("キャンセル")){
            return "redirect:/user/profile/credit-card/info";
        }

        creditCardService.delete(creditCardId);
        attrs.addFlashAttribute("success","カード情報の削除に成功しました");        
        if(source.equals("info")){
            return "redirect:/user/profile/credit-card/info";
        }
        return "redirect:/cart/view";
    }

    private CreditCardForm convertCreditCardForm(CreditCard creditCard){
        CreditCardForm creditCardForm = new CreditCardForm();
        creditCardForm.setCompany(creditCard.getCompany());
        creditCardForm.setName(creditCard.getName());
        creditCardForm.setCardNumber(creditCard.getCardNumber());
        creditCardForm.setExpMonth(creditCard.getExpMonth());
        creditCardForm.setExpYear(creditCard.getExpYear());
        creditCardForm.setCardDefault(creditCard.getCardDefault());
        return creditCardForm;
    }
    
    private CreditCard formToCreditCard(CreditCardForm creditCardForm, int creditCardId){
        CreditCard creditCard = creditCardService.getById(creditCardId);
        creditCard.setCompany(creditCardForm.getCompany());
        creditCard.setName(creditCardForm.getName());
        creditCard.setCardNumber(creditCardForm.getCardNumber());
        creditCard.setExpMonth(creditCardForm.getExpMonth());
        creditCard.setExpYear(creditCardForm.getExpYear());
        creditCard.setCardDefault(creditCardForm.getCardDefault());
        return creditCard;
    }

    private int currentYear(){
        LocalDate today = LocalDate.now();
        return today.getYear();
    }
    // public boolean checkExpireDate(CreditCard creditCard){
    //     LocalDate today = LocalDate.now();
    //     LocalDate creditCardDate = LocalDate.of(creditCard.getExpYear(), creditCard.getExpMonth(),1);
    //     return creditCardDate.isAfter(today);
    // }
}
