package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import jp.haru_idea.springboot.ec_site.models.Tax;
import jp.haru_idea.springboot.ec_site.services.TaxService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/setting/tax")
@Controller
public class TaxController {

    @Autowired
    private TaxService taxService;

    @GetMapping("/index")
    public String index(Model model){
        Collection<Tax> taxes = taxService.getAll();
        model.addAttribute("taxes", taxes);
        return "taxes/index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute Tax tax){
        return "taxes/create";
    }

    @PostMapping("/save")
    public String save(
            @Validated 
            @ModelAttribute Tax tax,
            BindingResult result,
            RedirectAttributes attrs){
        if(result.hasErrors()){
            return "taxes/create";
        }
        taxService.save(tax);
        attrs.addFlashAttribute("success", "登録が完了しました");
        return "redirect:/setting/tax/index";
    }

    @DeleteMapping("/delete/{taxId}")
    public String delete(@PathVariable int taxId, RedirectAttributes attrs){
        taxService.delete(taxId);
        attrs.addFlashAttribute("success", "データを削除しました");
        return "redirect:/setting/tax/index";
    }
}
