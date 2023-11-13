package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Collection;

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

import jp.haru_idea.springboot.ec_site.models.DiscontinuedFlag;
import jp.haru_idea.springboot.ec_site.models.Product;
import jp.haru_idea.springboot.ec_site.services.ProductService;

@RequestMapping("/product")
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/index")
    public String index(Model model){
        Collection<Product> products = productService.getAll();
        model.addAttribute("CONTINUED", DiscontinuedFlag.CONTINUED);
        model.addAttribute("DISCONTINUED", DiscontinuedFlag.DISCONTINUED);
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute Product product, Model model){
        // model.addAttribute("product", new Product());
        // model.addAttribute("discontinuedFlags", DiscontinuedFlag.values());
        return "products/create";        
    }

    @PostMapping("/save")
    public String save(
        @Validated    
        @ModelAttribute Product product,
        BindingResult result, Model model,
        RedirectAttributes attrs){
        if(result.hasErrors()){
            return "products/create";
        }
        productService.save(product);
        attrs.addFlashAttribute("success", "データの登録に成功しました。");
        return "redirect:/product/index";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        Product product = productService.getById(id);
        product.setOldCode(product.getName());
        model.addAttribute("product", product);
        return "products/edit";
    }

    @PatchMapping("/update/{id}")
    public String update(
        @PathVariable int id, 
        @Validated
        @ModelAttribute Product product,
        BindingResult result, Model model,
        RedirectAttributes attrs
        ){
            if(result.hasErrors()){
                return "products/edit";
            }
            product.setId(id);
            productService.save(product);
            attrs.addFlashAttribute("success", "データの更新に成功しました");
            return "redirect:/product/index";
    }
    
    @GetMapping("/show/{id}")
    public String show(@PathVariable int id, Model model){
        Product product = productService.getById(id);
        model.addAttribute("product", product);
        return "products/show";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model, RedirectAttributes attrs){
        productService.delete(id);
        attrs.addFlashAttribute("success","データの削除に成功しました");
        return "redirect:/product/index";
    }

    @GetMapping("/shopping/index")
    public String shoppingIndex(Model model){
        Collection<Product> products = productService.getByDiscontinuedFlag(0); 
        model.addAttribute("products", products);
        return "products/shoppings/index";
    }

    //TO DO html詳細画面の作成
    @GetMapping("/shopping/detail/{id}")
    public String detail(@PathVariable int id, Model model){
        Product product = productService.getById(id);
        model.addAttribute("product", product);
        return "products/shoppings/detail";
    }

}