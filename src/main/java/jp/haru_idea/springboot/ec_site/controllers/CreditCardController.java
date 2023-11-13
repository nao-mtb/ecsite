package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Calendar;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.haru_idea.springboot.ec_site.models.CreditCard;
import jp.haru_idea.springboot.ec_site.models.CreditCardForm;
import jp.haru_idea.springboot.ec_site.services.CreditCardService;

@RequestMapping("/user")
@Controller
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;

    @GetMapping("/credit-card/create/{userId}")
    public String create(
        @PathVariable int userId,
        @ModelAttribute CreditCard creditCard,
        Model model){
        model.addAttribute("userId", userId);
        model.addAttribute("year", currentYear());
        return "creditCards/create";
        }

    @PostMapping("/credit-card/save/{userId}")
    public String save(
        @PathVariable int userId,
        @Validated
        @ModelAttribute CreditCard creditCard,
        BindingResult result, Model model,
        RedirectAttributes attrs){
        if(result.hasErrors()){
            return "creditCards/create";
        }
        creditCardService.save(creditCard);
        //TODO addFlashAttribute 表示させる 
        attrs.addFlashAttribute("success","カード情報の登録に成功しました");
        return "redirect:/user/credit-card/create/" + userId;
    }
    

    @GetMapping("/{userId}/profile/credit-card/edit/{creditCardId}")
    public String editCreditCard(@PathVariable int userId, @PathVariable int creditCardId, Model model){
        CreditCard creditCard = creditCardService.getById(creditCardId);
        CreditCardForm creditCardForm = convertCreditCardForm(creditCard);
        model.addAttribute("userId", userId);
        model.addAttribute("creditCardForm", creditCardForm);
        model.addAttribute("year", currentYear());        
        return "creditCards/edit";
    }
    
    @Transactional
    @PatchMapping("/{userId}/profile/credit-card/update/{creditCardId}")
    public String updateCreditCard(
            @PathVariable int userId,
            @PathVariable int creditCardId,
            @Validated
            @ModelAttribute CreditCardForm creditCardForm,
            BindingResult result, Model model,
            RedirectAttributes attrs){
        if(result.hasErrors()){
            return "";
        }
        CreditCard creditCard = formToCreditCard(creditCardForm, creditCardId);
        creditCardService.save(creditCard);
        attrs.addFlashAttribute("success","データの更新に成功しました");        
        return "redirect:/user/" + userId + "/profile/";
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
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
}
