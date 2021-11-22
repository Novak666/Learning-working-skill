package com.changgou.user.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.pojo.Address;
import com.changgou.user.service.AddressService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    private TokenDecode tokenDecode;

    @Autowired
    public AddressController(AddressService  addressService) {
        super(addressService, Address.class);
        this.addressService = addressService;
    }

    //获取登录的用户名所对应的地址的列表
    @GetMapping("/user/list")
    public Result<List<Address>> list(){
        //1.获取当前的登录用户名
        String username = tokenDecode.getUsername();
        List<Address> addressList=addressService.list(username);
        return new Result<>(true, StatusCode.OK,"查询成功",addressList);
    }
}
