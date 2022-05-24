package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{id}/profile/edit/address")
    public String editAddress(@PathVariable int id, Model model){
        Address address = addressService.getById(id);
        AddressForm addressForm = convertAddressForm(address);
        model.addAttribute("addressForm", addressForm);
        return "users/edits/address";
    }
    
    @PatchMapping("/{id}/profile/update/address")



    @GetMapping("/address/index")
    public String index(Model model){
        Collection<Address> addresses = addressService.getAll();
        model.addAttribute("addresses",addresses);
        return "addresses/index";
    }

    @GetMapping("/address/create")
    public String create(@ModelAttribute Address address, Model model){
        return "addresses/create";
    }
    
    @GetMapping("/address/save")
    public String save(
        @Validated
        @ModelAttribute Address address,
        BindingResult result, Model model,
        RedirectAttributes attrs){
        if(result.hasErrors()){
            return "addresses/create";    
        }
        addressService.save(address);
        attrs.addFlashAttribute("address","データの登録に成功しました");
        return "redirect:/address/index";
    }

    private AddressForm convertAddressForm(Address address){
        AddressForm addressForm = new AddressForm();
        addressForm.setId(address.getId());
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
        address.setBillingDefault(address.getBillingDefault());
        return addressForm;
    }

    private Address formToAddress(AddressForm addressForm){
        Address address = addressService.getById(addressForm.getId());
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
        address.setBillingDefault(addressForm.getShippingDefault());
        return address;
    }
}
