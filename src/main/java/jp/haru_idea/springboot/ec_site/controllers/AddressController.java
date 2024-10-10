package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.haru_idea.springboot.ec_site.models.Address;
import jp.haru_idea.springboot.ec_site.models.AddressForm;
import jp.haru_idea.springboot.ec_site.models.CreditCard;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.services.AddressService;
import jp.haru_idea.springboot.ec_site.services.UserService;


@RequestMapping("/user")
@Controller
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private SecuritySession securitySession;

    @Autowired
    private UserService userService;

    @GetMapping("/profile/address/info")
    public String profile(Model model){
        int userId = securitySession.getUserId();
        model.addAttribute("addresses", addressService.getByUserId(userId));
        return "addresses/info";
    }

    @GetMapping("/profile/address/info/category")
    public String shippingProfile(@RequestParam("category") String category, Model model){
        int userId = securitySession.getUserId();
        int addressType = 0;
        //一覧表示のための変数設定,設定数字以外を表示（発送先兼請求書0、発送先1、請求書先2）
        if(category.equals("shipping")){
            addressType = 2;
        }else if(category.equals("billing")){
            addressType = 1;
        }
        model.addAttribute("addresses", addressService.getByUserIdAndAddressTypeNot(userId, addressType));
        return "addresses/info";
    }

    @GetMapping("/address/create")
    public String create(@ModelAttribute Address address){
        int userId = securitySession.getUserId();
        User user = userService.getById(userId);
        address.setUser(user);
        return "addresses/create";
    }
    
    @PostMapping("/address/save")
    public String save(
            @Validated 
            @ModelAttribute Address address,
            BindingResult result,
            RedirectAttributes attrs){
        if(result.hasErrors()){
            return "addresses/create";
        }
        addressService.save(address);
        attrs.addFlashAttribute("success","住所登録に成功しました");
        return "redirect:/user/credit-card/create"; 
    }

    @GetMapping("/profile/address/edit/{addressId}")
    public String editAddress(@PathVariable int addressId, Model model){
        int userId = securitySession.getUserId();
        Address address = addressService.getById(addressId);
        AddressForm addressForm = convertAddressForm(address);
        model.addAttribute("userId", userId);
        model.addAttribute("addressForm", addressForm);
        return "addresses/edit";
    }

    @Transactional
    @PatchMapping("/profile/address/update/{addressId}")
    public String updateAddress(
            @PathVariable int addressId,
            @Validated
            @ModelAttribute AddressForm addressForm,
            BindingResult result,
            RedirectAttributes attrs){
        int userId = securitySession.getUserId();
        if(result.hasErrors()){
            return "/users/profile/address/edit/" + addressId;
        }
        if(addressForm.getShippingDefault() == 1){
            addressService.resetShippingDefault(userId);
        }
        if(addressForm.getBillingDefault() == 1){
            addressService.resetBillingDefault(userId);            
        }
        Address address = formToAddress(addressForm, addressId);
        addressService.save(address);
        attrs.addFlashAttribute("success","データの更新に成功しました");        
        return "redirect:/user/profile/address/info";
    }

    //デフォルト登録先変更
    @Transactional
    @PatchMapping("/profile/address/update/default-shipping")
    public String changeShippingDefault(HttpServletRequest request, RedirectAttributes attrs){
        int userId = securitySession.getUserId();
        addressService.resetShippingDefault(userId);
        Address address = addressService.getById(Integer.parseInt(request.getParameter("addressId")));
        address.setShippingDefault(1);
        addressService.save(address);
        attrs.addFlashAttribute("success","いつも使用するお届け先に変更しました");
        return "redirect:/user/profile/address/info";
    }

    @Transactional
    @PatchMapping("/profile/address/update/default-billing")
    public String changeBillingDefault(HttpServletRequest request, RedirectAttributes attrs){
        int userId = securitySession.getUserId();
        addressService.resetBillingDefault(userId);
        Address address = addressService.getById(Integer.parseInt(request.getParameter("addressId")));
        address.setBillingDefault(1);
        addressService.save(address);
        attrs.addFlashAttribute("success","いつも使用する請求書送付先に変更しました");
        return "redirect:/user/profile/address/info";
    }

    @GetMapping("/address/index")
    public String index(Model model){
        Collection<Address> addresses = addressService.getAll();
        model.addAttribute("addresses",addresses);
        return "addresses/index";
    }


    //TODO バリデーションチェック機能追加
    private AddressForm convertAddressForm(Address address){
        AddressForm addressForm = new AddressForm();
        addressForm.setLastName(address.getLastName());
        addressForm.setFirstName(address.getFirstName());
        addressForm.setZipCode(address.getZipCode());
        addressForm.setPrefecture(address.getPrefecture());
        addressForm.setCity(address.getCity());
        addressForm.setAddress1(address.getAddress1());
        addressForm.setAddress2(address.getAddress2());
        addressForm.setTel(address.getTel());
        addressForm.setAddressType(address.getAddressType());
        addressForm.setShippingDefault(address.getShippingDefault());
        addressForm.setBillingDefault(address.getBillingDefault());
        return addressForm;
    }

    private Address formToAddress(AddressForm addressForm, int addressId){
        Address address = addressService.getById(addressId);
        address.setLastName(addressForm.getLastName());
        address.setFirstName(addressForm.getFirstName());
        address.setZipCode(addressForm.getZipCode());
        address.setPrefecture(addressForm.getPrefecture());
        address.setCity(addressForm.getCity());
        address.setAddress1(addressForm.getAddress1());
        address.setAddress2(addressForm.getAddress2());
        address.setTel(addressForm.getTel());
        address.setAddressType(addressForm.getAddressType());
        address.setShippingDefault(addressForm.getShippingDefault());
        address.setBillingDefault(addressForm.getBillingDefault());
        return address;
    }
}