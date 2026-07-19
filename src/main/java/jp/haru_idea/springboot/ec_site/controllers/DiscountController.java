package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import jp.haru_idea.springboot.ec_site.models.Discount;
import jp.haru_idea.springboot.ec_site.services.DiscountService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;


@RequestMapping("setting/discount")
@Controller
public class DiscountController {
    @Autowired
    public DiscountService discountService;

    @GetMapping("/index")
    public String index(Model model) {
        Collection<Discount> discount = discountService.getAll();
        model.addAttribute("discounts", discount);
        return "discounts/index";
    }
    
    @GetMapping("/create")
    public String create(@ModelAttribute Discount discount) {
        return "discounts/create";
    }

    @PostMapping("/save")
    public String save(
            @Validated
            @ModelAttribute Discount discount,
            BindingResult result,
            RedirectAttributes attr) {
        if(result.hasErrors()){
            return "discounts/create";
        }
        discountService.save(discount);
        attr.addFlashAttribute("success", "登録が完了しました");
        return "redirect:/setting/discount/index";
    }
    
    @GetMapping("/edit/{discountId}")
    public String edit(@PathVariable int discountId, Model model) {
        Discount discount = discountService.getById(discountId);
        model.addAttribute("discount", discount);
        return "discounts/edit";
    }
    
    @PatchMapping("/update")
    public String update(
            @Validated 
            @ModelAttribute Discount discount,
            BindingResult result, RedirectAttributes attr){
        if(result.hasErrors()){
            return "discounts/edit";
        }
        discountService.save(discount);
        attr.addFlashAttribute("success","更新しました");
        return "redirect:/setting/discount/index";
    }

    @DeleteMapping("/delete/{discountId}")
    public String delete(@PathVariable int discountId, RedirectAttributes attrs){
        discountService.delete(discountId);
        attrs.addFlashAttribute("success", "データを削除しました");
        return "redirect:/setting/discount/index";
    }
}
