package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Collection;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.haru_idea.springboot.ec_site.models.Address;
import jp.haru_idea.springboot.ec_site.models.AddressForm;
import jp.haru_idea.springboot.ec_site.services.AddressService;


@RequestMapping("/user")
@Controller
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/{userId}/profile/address/edit/{addressId}")
    public String editAddress(@PathVariable int userId, @PathVariable int addressId, Model model){
        Address address = addressService.getById(addressId);
        AddressForm addressForm = convertAddressForm(address);
        model.addAttribute("userId", userId);
        model.addAttribute("addressForm", addressForm);
        return "addresses/edit";
    }

    @Transactional
    @PatchMapping("/{userId}/profile/address/update/{addressId}")
    public String updateAddress(
            @PathVariable int userId,
            @PathVariable int addressId,
            @Validated
            @ModelAttribute AddressForm addressForm,
            BindingResult result, Model model,
            RedirectAttributes attrs){
        if(result.hasErrors()){
            return "/users/" + userId + "/profile/address/edit/" + addressId;
        }

        Address address = formToAddress(addressForm, addressId);
        if(address.getShippingDefault()==1){
            addressService.resetShippingDefault(userId);
        }
        if(address.getBillingDefault()==1){
            addressService.resetBillingDefault(userId);            
        }
        addressService.save(address);
        attrs.addFlashAttribute("success","データの更新に成功しました");        
        return "redirect:/user/" + userId + "/profile/";
    }


    @GetMapping("/address/index")
    public String index(Model model){
        Collection<Address> addresses = addressService.getAll();
        model.addAttribute("addresses",addresses);
        return "addresses/index";
    }

    @GetMapping("/address/create/{userId}")
    public String create(
        @PathVariable int userId,
        @ModelAttribute Address address, 
        Model model){
        model.addAttribute("userId", userId);
        return "addresses/create";
    }
    
    @PostMapping("/address/save/{userId}")
    public String save(
        @PathVariable int userId,
        @Validated
        @ModelAttribute Address address,
        BindingResult result, Model model,
        RedirectAttributes attrs){
        if(result.hasErrors()){
            return "addresses/create";    
        }
        addressService.save(address);
        attrs.addFlashAttribute("success","住所登録に成功しました");
        //TODO クレジットカード登録先に変更
        //return "redirect:/user/address/create/" + userId;
        return "redirect:/user/credit-card/create/" + userId; 
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
