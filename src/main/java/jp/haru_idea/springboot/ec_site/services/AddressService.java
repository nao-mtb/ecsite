package jp.haru_idea.springboot.ec_site.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.Address;
import jp.haru_idea.springboot.ec_site.repositories.AddressRepository;

@Service
public class AddressService {
    @Autowired AddressRepository addressRepository;

    public Collection<Address> getAll(){
        return addressRepository.findAll();
    }
    
    public void save(Address address){
        addressRepository.save(address);
    }

    public void delete(int id){
        addressRepository.deleteById(id);
    }

    public Address getById(int id){
        return addressRepository.findById(id);
    }
}
