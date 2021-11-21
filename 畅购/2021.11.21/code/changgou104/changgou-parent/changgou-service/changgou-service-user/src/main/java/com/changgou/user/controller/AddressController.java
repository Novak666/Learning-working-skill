package com.changgou.user.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.user.pojo.Address;
import com.changgou.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/address")
@CrossOrigin
public class AddressController extends AbstractCoreController<Address>{

    private AddressService  addressService;

    @Autowired
    public AddressController(AddressService  addressService) {
        super(addressService, Address.class);
        this.addressService = addressService;
    }
}
